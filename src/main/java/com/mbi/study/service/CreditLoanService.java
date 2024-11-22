package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCreditLoanRequest;
import com.mbi.study.controller.dto.CreateCreditLoanResponse;

public interface CreditLoanService {
    CreateCreditLoanResponse create(CreateCreditLoanRequest createCreditLoanRequest);
}
