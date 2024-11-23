package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateLoanResponse;
import com.mbi.study.controller.dto.GetAllCustomerLoanRequest;
import com.mbi.study.controller.dto.LoanResponse;
import com.mbi.study.common.exception.CustomerNotEnoughLimitForLoanException;
import com.mbi.study.common.mapper.LoanMapper;
import com.mbi.study.repository.LoanRepository;
import com.mbi.study.repository.entity.Customer;
import com.mbi.study.repository.entity.Loan;
import com.mbi.study.repository.entity.LoanInstallment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Override
    @Transactional
    public CreateLoanResponse create(CreateCreditLoanRequest createCreditLoanRequest) {
        Long customerId = createCreditLoanRequest.getCustomerId();
        Customer customer = customerService.getById(customerId);
        if (!customer.hasEnoughLimit(createCreditLoanRequest.getAmount())) {
            throw new CustomerNotEnoughLimitForLoanException("Cannot create loan since user does not has enough limit");
        }

        BigDecimal totalLoanAmount = calculateTotalLoanAmount(createCreditLoanRequest);

        // save loan with installments
        Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(totalLoanAmount)
                .isPaid(false)
                .build();
        List<LoanInstallment> installments = createInstallments(loan, totalLoanAmount, createCreditLoanRequest);
        loan.setInstallments(installments);
        loan.setNumberOfInstallment(installments.size());
        loanRepository.save(loan);

        customerService.updateUsedCreditLimit(customer, totalLoanAmount);

        BigDecimal monthlyInstallmentAmount = installments.stream().findAny().map(LoanInstallment::getAmount).orElse(BigDecimal.ZERO);

        return new CreateLoanResponse(monthlyInstallmentAmount, totalLoanAmount, totalLoanAmount.subtract(createCreditLoanRequest.getAmount()));
    }

    private static BigDecimal calculateMonthlyPaymentAmount(CreateCreditLoanRequest createCreditLoanRequest, BigDecimal totalLoanAmount) {
        return totalLoanAmount.divide(BigDecimal.valueOf(createCreditLoanRequest.getNumberOfInstallments()), 2, RoundingMode.HALF_UP);
    }

    private List<LoanInstallment> createInstallments(Loan loan, BigDecimal totalLoanAmount, CreateCreditLoanRequest createCreditLoanRequest) {
        List<LoanInstallment> installments = new ArrayList<>();
        BigDecimal installmentAmountPerMonth = calculateMonthlyPaymentAmount(createCreditLoanRequest, totalLoanAmount);
        for (int installmentIndex = 0; installmentIndex < createCreditLoanRequest.getNumberOfInstallments(); installmentIndex++) {
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
        Long customerId = allCustomerLoanRequest.getCustomerId(); // TODO think to use JPA Specifications
        return loanRepository.getLoansByCustomerId(customerId).stream().map(loanMapper::toLoanResponse).toList();
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loanMapper::toLoanResponse)
                .orElseThrow(EntityNotFoundException::new);
    }

    private BigDecimal calculateTotalLoanAmount(CreateCreditLoanRequest createCreditLoanRequest) {
        // amount * (1 + interest rate)
        return createCreditLoanRequest.getAmount().multiply(BigDecimal.valueOf(1 + createCreditLoanRequest.getInterestRate()));
    }


}
