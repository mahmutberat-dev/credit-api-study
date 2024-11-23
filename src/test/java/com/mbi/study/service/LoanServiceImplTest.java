package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateLoanResponse;
import com.mbi.study.controller.dto.PayLoanRequest;
import com.mbi.study.controller.dto.PayLoanResponse;
import com.mbi.study.repository.LoanInstallmentRepository;
import com.mbi.study.repository.LoanRepository;
import com.mbi.study.repository.entity.Customer;
import com.mbi.study.repository.entity.Loan;
import com.mbi.study.repository.entity.LoanInstallment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;
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
        customer.setUsedCreditLimit(BigDecimal.ZERO);

        when(customerService.getById(createCreditLoanRequest.getCustomerId())).thenReturn(customer);
        when(loanRepository.save(any())).thenAnswer(argument -> {
            Loan argument1 = (Loan) argument.getArguments()[0];
            argument1.setId(100L);
            return argument1;
        });

        CreateLoanResponse loanResponse = creditLoanService.create(createCreditLoanRequest);

        ArgumentCaptor<Loan> loanArgumentCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanArgumentCaptor.capture());
        Loan captorValue = loanArgumentCaptor.getValue();
        assertEquals(captorValue.getCustomer().getId(), createCreditLoanRequest.getCustomerId());
        assertEquals(BigDecimal.valueOf(110_000.0), captorValue.getLoanAmount());
        assertEquals(12, captorValue.getNumberOfInstallment());
        assertEquals(12, captorValue.getInstallments().size());

        verify(customerService).updateUsedCreditLimit(customer, BigDecimal.valueOf(110_000.0));

        assertEquals(BigDecimal.valueOf(9_166.67), loanResponse.monthlyInstallmentAmount());
        assertEquals(BigDecimal.valueOf(110_000.0), loanResponse.totalPayment());
        assertEquals(BigDecimal.valueOf(10_000.0), loanResponse.totalInterest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {2500, 2900, 3000, 3500, 4999})
    void shouldPaySingleLoanInstallment(double amount) {
        double monthlyInstallmentAmount = 2500;
        PayLoanRequest payLoanRequest = new PayLoanRequest();
        payLoanRequest.setLoanId(5001L);
        payLoanRequest.setAmount(amount);
        payLoanRequest.setCustomerId(1001L);

        Customer customer = new Customer();
        customer.setId(1001L);

        Loan loan = new Loan();
        loan.setId(3001L);
        loan.setCustomer(customer);
        LoanInstallment loanInstallment1 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 1);
        LoanInstallment loanInstallment2 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 2);
        LoanInstallment loanInstallment3 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 3);
        LoanInstallment loanInstallment4 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 4);
        LoanInstallment loanInstallment5 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 5);
        LoanInstallment loanInstallment6 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 6);

        loan.setInstallments(List.of(loanInstallment1, loanInstallment2, loanInstallment3, loanInstallment4, loanInstallment5, loanInstallment6));

        when(customerService.getById(payLoanRequest.getCustomerId())).thenReturn(customer);
        when(loanRepository.findById(5001L)).thenReturn(Optional.of(loan));

        PayLoanResponse payLoanResponse = creditLoanService.payLoan(payLoanRequest);

        assertFalse(payLoanResponse.isLoanPaid());
        assertEquals(1, payLoanResponse.installmentsPaid());
        assertTrue(BigDecimal.valueOf(2500).compareTo(payLoanResponse.amountPaid()) == 0);

        verify(loanRepository, never()).save(any());

        ArgumentCaptor<LoanInstallment> loanInstallmentArgumentCaptor = ArgumentCaptor.forClass(LoanInstallment.class);
        verify(loanInstallmentRepository).save(loanInstallmentArgumentCaptor.capture());
        LoanInstallment savedLoanInstallment = loanInstallmentArgumentCaptor.getValue();
        assertEquals(1, savedLoanInstallment.getId());
        assertTrue(BigDecimal.valueOf(2500).compareTo(savedLoanInstallment.getPaidAmount()) == 0);
        assertNotNull(savedLoanInstallment.getPaymentDate());
        assertTrue(savedLoanInstallment.isPaid());
    }

    @ParameterizedTest
    @ValueSource(doubles = {5000, 5001, 7499})
    void shouldPayTwiceLoanInstallments(double amount) {
        double monthlyInstallmentAmount = 2500;
        PayLoanRequest payLoanRequest = new PayLoanRequest();
        payLoanRequest.setLoanId(5001L);
        payLoanRequest.setAmount(amount);
        payLoanRequest.setCustomerId(1001L);

        Customer customer = new Customer();
        customer.setId(1001L);

        Loan loan = new Loan();
        loan.setId(3001L);
        loan.setCustomer(customer);
        LoanInstallment loanInstallment1 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 1);
        LoanInstallment loanInstallment2 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 2);
        LoanInstallment loanInstallment3 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 3);
        LoanInstallment loanInstallment4 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 4);
        LoanInstallment loanInstallment5 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 5);
        LoanInstallment loanInstallment6 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 6);

        loan.setInstallments(List.of(loanInstallment1, loanInstallment2, loanInstallment3, loanInstallment4, loanInstallment5, loanInstallment6));

        when(customerService.getById(payLoanRequest.getCustomerId())).thenReturn(customer);
        when(loanRepository.findById(5001L)).thenReturn(Optional.of(loan));

        PayLoanResponse payLoanResponse = creditLoanService.payLoan(payLoanRequest);

        assertFalse(payLoanResponse.isLoanPaid());
        assertEquals(2, payLoanResponse.installmentsPaid());
        assertTrue(BigDecimal.valueOf(5000).compareTo(payLoanResponse.amountPaid()) == 0);

        verify(loanRepository, never()).save(any());

        ArgumentCaptor<LoanInstallment> loanInstallmentArgumentCaptor = ArgumentCaptor.forClass(LoanInstallment.class);
        verify(loanInstallmentRepository, times(2)).save(loanInstallmentArgumentCaptor.capture());
        List<LoanInstallment> savedLoanInstallments = loanInstallmentArgumentCaptor.getAllValues();
        LoanInstallment savedLoanInstallment1 = savedLoanInstallments.get(0);
        LoanInstallment savedLoanInstallment2 = savedLoanInstallments.get(1);
        assertEquals(1, savedLoanInstallment1.getId());
        assertEquals(2, savedLoanInstallment2.getId());
        assertTrue(BigDecimal.valueOf(2500).compareTo(savedLoanInstallment1.getPaidAmount()) == 0);
        assertTrue(BigDecimal.valueOf(2500).compareTo(savedLoanInstallment2.getPaidAmount()) == 0);
        assertNotNull(savedLoanInstallment1.getPaymentDate());
        assertNotNull(savedLoanInstallment2.getPaymentDate());
        assertTrue(savedLoanInstallment1.isPaid());
        assertTrue(savedLoanInstallment2.isPaid());
    }

    @ParameterizedTest
    @ValueSource(doubles = {7500})
    void shouldPayRemainedLoanInstallments(double amount) {
        double monthlyInstallmentAmount = 2500;
        PayLoanRequest payLoanRequest = new PayLoanRequest();
        payLoanRequest.setLoanId(5001L);
        payLoanRequest.setAmount(amount);
        payLoanRequest.setCustomerId(1001L);

        Customer customer = new Customer();
        customer.setId(1001L);

        Loan loan = new Loan();
        loan.setId(3001L);
        loan.setCustomer(customer);
        LoanInstallment loanInstallment1 = createLoanInterestStub(loan, monthlyInstallmentAmount, true, 1);
        LoanInstallment loanInstallment2 = createLoanInterestStub(loan, monthlyInstallmentAmount, true, 2);
        LoanInstallment loanInstallment3 = createLoanInterestStub(loan, monthlyInstallmentAmount, true, 3);
        LoanInstallment loanInstallment4 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 4);
        LoanInstallment loanInstallment5 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 5);
        LoanInstallment loanInstallment6 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 6);

        loan.setInstallments(List.of(loanInstallment1, loanInstallment2, loanInstallment3, loanInstallment4, loanInstallment5, loanInstallment6));

        when(customerService.getById(payLoanRequest.getCustomerId())).thenReturn(customer);
        when(loanRepository.findById(5001L)).thenReturn(Optional.of(loan));

        PayLoanResponse payLoanResponse = creditLoanService.payLoan(payLoanRequest);

        assertTrue(payLoanResponse.isLoanPaid());
        assertEquals(3, payLoanResponse.installmentsPaid());
        assertTrue(BigDecimal.valueOf(7500).compareTo(payLoanResponse.amountPaid()) == 0);
        verify(loanRepository, times(1)).save(any());
        verify(loanInstallmentRepository, times(3)).save(any());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 2499, 100, 1000, 10_000, 15_000})
    void shouldNotPayLoanInstallment(double amount) {
        double monthlyInstallmentAmount = 2500;
        PayLoanRequest payLoanRequest = new PayLoanRequest();
        payLoanRequest.setLoanId(5001L);
        payLoanRequest.setAmount(amount);
        payLoanRequest.setCustomerId(1001L);

        Customer customer = new Customer();
        customer.setId(1001L);

        Loan loan = new Loan();
        loan.setId(3001L);
        loan.setCustomer(customer);
        LoanInstallment loanInstallment1 = createLoanInterestStub(loan, monthlyInstallmentAmount, false, 1);

        loan.setInstallments(List.of(loanInstallment1));

        when(customerService.getById(payLoanRequest.getCustomerId())).thenReturn(customer);
        when(loanRepository.findById(5001L)).thenReturn(Optional.of(loan));

        assertThrows(IllegalArgumentException.class, () -> creditLoanService.payLoan(payLoanRequest));
    }

    private LoanInstallment createLoanInterestStub(Loan loan, double installmentAmount, boolean isPaid, long id) {
        LoanInstallment loanInstallment = new LoanInstallment();
        loanInstallment.setId(id);
        loanInstallment.setLoan(loan);
        loanInstallment.setAmount(BigDecimal.valueOf(installmentAmount));
        loanInstallment.setPaid(isPaid);
        return loanInstallment;
    }
}