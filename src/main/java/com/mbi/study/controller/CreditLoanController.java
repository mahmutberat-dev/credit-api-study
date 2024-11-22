package com.mbi.study.controller;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.service.CreditLoanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class CreditLoanController {

    CreditLoanService creditLoanService;
    @PostMapping
    public boolean creditLoan(@Valid @RequestBody CreateCreditLoanRequest createCreditLoanRequest) {

        return true;
    }

}
