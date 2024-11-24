package com.mbi.study.service;

import com.mbi.study.common.exception.CustomerNotEnoughLimitForLoanException;
import com.mbi.study.common.mapper.LoanMapper;
import com.mbi.study.controller.dto.*;
import com.mbi.study.repository.LoanInstallmentRepository;
import com.mbi.study.repository.LoanRepository;
import com.mbi.study.repository.entity.User;
import com.mbi.study.repository.entity.Loan;
import com.mbi.study.repository.entity.LoanInstallment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final UserService userService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanMapper loanMapper;

    @Override
    @Transactional
    public CreateLoanResponse create(CreateCreditLoanRequest createCreditLoanRequest) {
        User user = getUserToCreateLoan(createCreditLoanRequest);

        BigDecimal totalLoanAmount = calculateTotalLoanAmount(createCreditLoanRequest);

        // save loan with installments
        Loan loan = Loan.builder()
                .user(user)
                .loanAmount(totalLoanAmount)
                .isPaid(false)
                .build();
        List<LoanInstallment> installments = createInstallments(loan, totalLoanAmount, createCreditLoanRequest);
        loan.setInstallments(installments);
        loan.setNumberOfInstallment(installments.size());
        Loan savedLoan = saveLoan(loan);

        userService.updateUsedCreditLimit(user, totalLoanAmount);

        BigDecimal monthlyInstallmentAmount = installments.stream().findAny().map(LoanInstallment::getAmount).orElse(BigDecimal.ZERO);

        return new CreateLoanResponse(savedLoan.getId(), monthlyInstallmentAmount, totalLoanAmount, totalLoanAmount.subtract(createCreditLoanRequest.amount()));
    }

    private User getUserToCreateLoan(CreateCreditLoanRequest createCreditLoanRequest) {
        User user = userService.getById(createCreditLoanRequest.customerId());
        if (!user.hasEnoughLimit(createCreditLoanRequest.amount())) {
            throw new CustomerNotEnoughLimitForLoanException("Cannot create loan since user does not has enough limit");
        }
        return user;
    }

    private static BigDecimal calculateMonthlyPaymentAmount(CreateCreditLoanRequest createCreditLoanRequest, BigDecimal totalLoanAmount) {
        return totalLoanAmount.divide(BigDecimal.valueOf(createCreditLoanRequest.numberOfInstallments()), 2, RoundingMode.HALF_UP);
    }

    private List<LoanInstallment> createInstallments(Loan loan, BigDecimal totalLoanAmount, CreateCreditLoanRequest createCreditLoanRequest) {
        List<LoanInstallment> installments = new ArrayList<>();
        BigDecimal installmentAmountPerMonth = calculateMonthlyPaymentAmount(createCreditLoanRequest, totalLoanAmount);
        for (int installmentIndex = 0; installmentIndex < createCreditLoanRequest.numberOfInstallments(); installmentIndex++) {
            LoanInstallment loanInstallment = LoanInstallment.builder()
                    .loan(loan)
                    .amount(installmentAmountPerMonth)
                    .paidAmount(BigDecimal.ZERO)
                    .isPaid(false)
                    .dueDate(calculateInstallmentDueDate(installmentIndex + 1))
                    .build();
            installments.add(loanInstallment);
        }
        return installments;
    }

    private Date calculateInstallmentDueDate(int installmentIndex) {
        Instant instant = YearMonth.now(ZoneOffset.UTC)
                .plusMonths(installmentIndex)
                .atDay(1)
                .atTime(17, 0, 0)
                .toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    @Override
    public List<LoanResponse> getLoans(GetAllCustomerLoanRequest allCustomerLoanRequest) {
        Long customerId = allCustomerLoanRequest.customerId(); // TODO think to use JPA Specifications
        return loanRepository.getLoansByUserId(customerId).stream().map(loanMapper::toLoanResponse).toList();
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {
        return Optional.of(getById(loanId))
                .map(loanMapper::toLoanResponse)
                .orElseThrow(EntityNotFoundException::new);
    }

    private Loan getById(Long loanId) {
        return loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public PayLoanResponse payLoan(PayLoanRequest payLoanRequest) {
        User user = userService.getById(payLoanRequest.customerId());
        Loan loan = getById(payLoanRequest.loanId());

        validateLoanPayment(loan, user);

        BigDecimal monthlyInstallmentAmount = loan.getInstallments().stream().findAny().map(LoanInstallment::getAmount).orElse(BigDecimal.ZERO);
        int validateLoanFactor = validateLoanAmount(payLoanRequest, monthlyInstallmentAmount);

        PayInstallmentDTO payInstallmentDTO = payInstallments(loan, validateLoanFactor);

        if (isAllLoanInstallmentsArePaid(payInstallmentDTO.loanInstallments())) {
            loan.setPaid(true);
            saveLoan(loan);
            // customerService.freeUsedCreditLimit(customer, loan.getLoanAmount()); // TODO shall the usedCreditLimit be updated?
        }
        return new PayLoanResponse(validateLoanFactor, payInstallmentDTO.paidAmount, loan.isPaid());
    }

    private Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    private PayInstallmentDTO payInstallments(Loan loan, int validateLoanFactor) {
        Date currentDate = Date.from(Instant.now());
        BigDecimal paidAmount = BigDecimal.ZERO;

        List<LoanInstallment> loanInstallments = loan.getInstallments().stream()
                .filter(loanInstallment -> !loanInstallment.isPaid()).toList();
        for (int installmentIndex = 0; installmentIndex < validateLoanFactor; installmentIndex++) {
            if (installmentIndex < loanInstallments.size()) {
                LoanInstallment loanInstallment = loanInstallments.get(installmentIndex);
                loanInstallment.setPaid(true);
                loanInstallment.setPaymentDate(currentDate);
                if (DateUtils.isSameDay(loanInstallment.getDueDate(), currentDate)) { // same day
                    paidAmount = paidAmount.add(loanInstallment.getAmount());
                    loanInstallment.setPaidAmount(loanInstallment.getAmount());
                } else if (currentDate.before(loanInstallment.getDueDate())) { // reward
                    BigDecimal rewardedLoan = loanInstallment.getAmount().subtract(loanInstallment.getAmount().divide(BigDecimal.valueOf(1000), 2, RoundingMode.FLOOR));
                    paidAmount = paidAmount.add(rewardedLoan);
                    loanInstallment.setPaidAmount(rewardedLoan);
                } else { // penalty
                    BigDecimal rewardedLoan = loanInstallment.getAmount().add(loanInstallment.getAmount().divide(BigDecimal.valueOf(1000), 2, RoundingMode.FLOOR));
                    paidAmount = paidAmount.add(rewardedLoan);
                    loanInstallment.setPaidAmount(loanInstallment.getAmount());
                }
                loanInstallmentRepository.save(loanInstallment);
            }
        }
        return new PayInstallmentDTO(loanInstallments, paidAmount);
    }

    private void validateLoanPayment(Loan loan, User user) {
        if (loan.isPaid()) {
            throw new IllegalStateException(String.format("The Loan is already paid. Loan Id: %d", loan.getId()));
        }
        if (!loan.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException(String.format("Cannot pay loan. Loan owner id: %s is not matched with requested user id: %s", loan.getUser().getId(), user.getId()));
        }
    }

    private boolean isAllLoanInstallmentsArePaid(List<LoanInstallment> loanInstallments) {
        return loanInstallments.stream().filter(loanInstallment -> !loanInstallment.isPaid()).findAny().isEmpty();
    }

    private int validateLoanAmount(PayLoanRequest payLoanRequest, BigDecimal monthlyInstallmentAmount) {
        BigDecimal amountBigDecimal = BigDecimal.valueOf(payLoanRequest.amount());
        int amountFactor = amountBigDecimal.divide(monthlyInstallmentAmount, 2, RoundingMode.FLOOR).intValue();
        if (amountFactor <= 0 || amountFactor > 3) {
            throw new IllegalArgumentException("Loan pay amount is less than a single installment amount or more than 3 times of a single installment amount");
        }
        return amountFactor;
    }

    private BigDecimal calculateTotalLoanAmount(CreateCreditLoanRequest createCreditLoanRequest) {
        // amount * (1 + interest rate)
        return createCreditLoanRequest.amount().multiply(BigDecimal.valueOf(1 + createCreditLoanRequest.interestRate()));
    }


    private record PayInstallmentDTO(List<LoanInstallment> loanInstallments, BigDecimal paidAmount) {

    }
}
