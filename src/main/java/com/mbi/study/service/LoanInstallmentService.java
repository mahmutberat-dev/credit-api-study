package com.mbi.study.service;

import com.mbi.study.controller.dto.LoanInstallmentResponse;

import java.util.List;

public interface LoanInstallmentService {

    List<LoanInstallmentResponse> getByLoanId(Long loanId);
}
