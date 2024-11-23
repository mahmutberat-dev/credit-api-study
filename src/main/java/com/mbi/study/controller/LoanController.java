package com.mbi.study.controller;

import com.mbi.study.controller.dto.*;
import com.mbi.study.service.LoanService;
import com.mbi.study.service.LoanInstallmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;
    private final LoanInstallmentService loanInstallmentService;

    @PostMapping
    public CreateLoanResponse createLoan(@Valid @RequestBody CreateCreditLoanRequest createCreditLoanRequest) {
        return loanService.create(createCreditLoanRequest);
    }

    @PutMapping
    public PayLoanResponse payLoan(@Valid @RequestBody PayLoanRequest payLoanRequest) {
        return loanService.payLoan(payLoanRequest);
    }

    @GetMapping()
    public List<LoanResponse> getCustomerLoans(GetAllCustomerLoanRequest allCustomerLoanRequest) {
        return loanService.getLoans(allCustomerLoanRequest);
    }

    @GetMapping("/{loanId}")
    public LoanResponse getCustomerLoans(@PathVariable Long loanId) {
        return loanService.getLoanById(loanId);
    }

    @GetMapping("/{loanId}/installment")
    public List<LoanInstallmentResponse> getCustomerLoanInstallments(@PathVariable Long loanId) {
        return loanInstallmentService.getByLoanId(loanId);
    }

}
