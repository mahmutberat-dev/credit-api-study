package com.mbi.study.controller;

import com.mbi.study.controller.dto.*;
import com.mbi.study.service.LoanInstallmentService;
import com.mbi.study.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;
    private final LoanInstallmentService loanInstallmentService;

    @Operation(summary = "Create Loan with Requested Installments for User")
    @PostMapping
    @PreAuthorize("#createCreditLoanRequest.customerId() == authentication.principal.id or hasAuthority('ADMIN')")
    public CreateLoanResponse createLoan(@Valid @RequestBody CreateCreditLoanRequest createCreditLoanRequest) {
        return loanService.create(createCreditLoanRequest);
    }

    @Operation(summary = "Update Loan to Pay an Installment")
    @PutMapping
    @PreAuthorize("#payLoanRequest.customerId() == authentication.principal.id or hasAuthority('ADMIN')")
    public PayLoanResponse payLoan(@Valid @RequestBody PayLoanRequest payLoanRequest) {
        return loanService.payLoan(payLoanRequest);
    }

    @Operation(summary = "Get All Customer Loans and Installments")
    @GetMapping()
    @PreAuthorize("#customerId == authentication.principal.id or hasAuthority('ADMIN')")
    public List<LoanResponse> getCustomerLoansById(@RequestParam Long customerId) {
        return loanService.getLoans(new GetAllCustomerLoanRequest(customerId, 0, false));
    }

    @Operation(summary = "Get Single Customer Loan and Installments")
    @GetMapping("/{loanId}")
    @PostAuthorize("returnObject.userId() == authentication.principal.id or hasAuthority('ADMIN')")
    public LoanResponse getCustomerLoans(@PathVariable Long loanId) {
        return loanService.getLoanById(loanId);
    }

    @Operation(summary = "Get Customer Loan Installments")
    @GetMapping("/{loanId}/installment")
    public List<LoanInstallmentResponse> getCustomerLoanInstallments(@PathVariable Long loanId) {
        return loanInstallmentService.getByLoanId(loanId);
    }

}
