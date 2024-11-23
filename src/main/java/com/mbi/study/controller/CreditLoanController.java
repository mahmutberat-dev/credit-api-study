package com.mbi.study.controller;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.GetAllCustomerLoanRequest;
import com.mbi.study.controller.dto.LoanInstallmentResponse;
import com.mbi.study.controller.dto.LoanResponse;
import com.mbi.study.service.CreditLoanService;
import com.mbi.study.service.LoanInstallmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class CreditLoanController {

    private final CreditLoanService creditLoanService;
    private final LoanInstallmentService loanInstallmentService;

    @PostMapping
    public boolean createLoan(@Valid @RequestBody CreateCreditLoanRequest createCreditLoanRequest) {
        creditLoanService.create(createCreditLoanRequest);
        return true;
    }

    @GetMapping()
    public List<LoanResponse> getCustomerLoans(GetAllCustomerLoanRequest allCustomerLoanRequest) {
        return creditLoanService.getLoans(allCustomerLoanRequest);
    }

    @GetMapping("/{loanId}")
    public LoanResponse getCustomerLoans(@PathVariable Long loanId) {
        return creditLoanService.getLoanById(loanId);
    }

    @GetMapping("/{loanId}/installment")
    public List<LoanInstallmentResponse> getCustomerLoanInstallments(@PathVariable Long loanId) {
        return loanInstallmentService.getByLoanId(loanId);
    }

}
