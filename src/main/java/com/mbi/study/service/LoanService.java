package com.mbi.study.service;

import com.mbi.study.controller.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface LoanService {
    CreateLoanResponse create(CreateCreditLoanRequest createCreditLoanRequest);

    List<LoanResponse> getLoans(GetAllCustomerLoanRequest allCustomerLoanRequest);

    LoanResponse getLoanById(Long loanId);

    PayLoanResponse payLoan(@Valid PayLoanRequest payLoanRequest);
}
