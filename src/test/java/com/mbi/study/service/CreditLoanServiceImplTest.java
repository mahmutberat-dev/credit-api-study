package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateCreditLoanResponse;
import com.mbi.study.repository.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditLoanServiceImplTest {

    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CreditLoanServiceImpl creditLoanService;

    @Test
    void shouldCreateLoan() {
        CreateCreditLoanRequest createCreditLoanRequest = new CreateCreditLoanRequest();
        createCreditLoanRequest.setCustomerId(1001L);
        createCreditLoanRequest.setAmount(BigDecimal.valueOf(100_000));
        createCreditLoanRequest.setNumberOfInstallments(12);
        createCreditLoanRequest.setInterestRate(0.1);

        Customer customer = new Customer();
        customer.setId(1001L);
        customer.setCreditLimit(BigDecimal.valueOf(100_000));
        customer.setUsedCreditLimit(null);

        when(customerService.getById(createCreditLoanRequest.getCustomerId())).thenReturn(customer);

        CreateCreditLoanResponse loanResponse = creditLoanService.create(createCreditLoanRequest);

        assertEquals(BigDecimal.valueOf(9_166.67), loanResponse.getMonthlyInstallmentAmount());
        assertEquals(BigDecimal.valueOf(110_000.0), loanResponse.getTotalPayment());
        assertEquals(BigDecimal.valueOf(10_000.0), loanResponse.getTotalInterest());
    }
}