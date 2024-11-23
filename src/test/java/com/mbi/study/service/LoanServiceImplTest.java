package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateLoanResponse;
import com.mbi.study.repository.LoanRepository;
import com.mbi.study.repository.entity.Customer;
import com.mbi.study.repository.entity.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanServiceImpl creditLoanService;

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

        CreateLoanResponse loanResponse = creditLoanService.create(createCreditLoanRequest);

        ArgumentCaptor<Loan> loanArgumentCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanArgumentCaptor.capture());
        Loan captorValue = loanArgumentCaptor.getValue();
        assertEquals(captorValue.getCustomer().getId(), createCreditLoanRequest.getCustomerId());
        assertEquals(BigDecimal.valueOf(110_000.0), captorValue.getLoanAmount());
        assertEquals(12, captorValue.getNumberOfInstallment());
        assertEquals(12, captorValue.getInstallments().size());

        assertEquals(BigDecimal.valueOf(9_166.67), loanResponse.getMonthlyInstallmentAmount());
        assertEquals(BigDecimal.valueOf(110_000.0), loanResponse.getTotalPayment());
        assertEquals(BigDecimal.valueOf(10_000.0), loanResponse.getTotalInterest());
    }
}