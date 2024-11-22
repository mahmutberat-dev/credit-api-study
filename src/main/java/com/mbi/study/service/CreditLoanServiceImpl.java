package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateCreditLoanResponse;
import com.mbi.study.exception.LoanNotEnoughLimitException;
import com.mbi.study.repository.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditLoanServiceImpl implements CreditLoanService {
    private final CustomerService customerService;

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

    private BigDecimal calculateTotalLoanAmount(CreateCreditLoanRequest createCreditLoanRequest) {
        // amount * (1 + interest rate)
        return createCreditLoanRequest.getAmount().multiply(BigDecimal.valueOf(1 + createCreditLoanRequest.getInterestRate()));
    }

}
