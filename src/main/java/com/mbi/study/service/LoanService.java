package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateLoanResponse;
import com.mbi.study.controller.dto.GetAllCustomerLoanRequest;
import com.mbi.study.controller.dto.LoanResponse;

import java.util.List;

public interface LoanService {
    CreateLoanResponse create(CreateCreditLoanRequest createCreditLoanRequest);

    List<LoanResponse> getLoans(GetAllCustomerLoanRequest allCustomerLoanRequest);

    LoanResponse getLoanById(Long loanId);
}
