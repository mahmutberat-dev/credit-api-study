package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateCreditLoanResponse;
import com.mbi.study.controller.dto.GetAllCustomerLoanRequest;
import com.mbi.study.controller.dto.LoanResponse;
import com.mbi.study.common.exception.LoanNotEnoughLimitException;
import com.mbi.study.common.mapper.LoanMapper;
import com.mbi.study.repository.LoanRepository;
import com.mbi.study.repository.entity.Customer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditLoanServiceImpl implements CreditLoanService {
    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Override
    public CreateCreditLoanResponse create(CreateCreditLoanRequest createCreditLoanRequest) {
        Long customerId = createCreditLoanRequest.getCustomerId();
        Customer customer = customerService.getById(customerId);
        if (!customer.hasEnoughLimit(createCreditLoanRequest.getAmount())) {
            throw new LoanNotEnoughLimitException("Cannot create loan since user does not has enough limit");
        }

        BigDecimal loanAmountDetails = calculateTotalLoanAmount(createCreditLoanRequest);

        // save loan and installments
        return new CreateCreditLoanResponse(loanAmountDetails.divide(BigDecimal.valueOf(createCreditLoanRequest.getNumberOfInstallments()), 2, RoundingMode.HALF_UP), loanAmountDetails, loanAmountDetails.subtract(createCreditLoanRequest.getAmount()));
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
