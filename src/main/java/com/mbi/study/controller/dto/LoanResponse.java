package com.mbi.study.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record LoanResponse(Long loanId,
                           long userId,
                           List<LoanInstallmentResponse> installments,
                           BigDecimal loanAmount,
                           int numberOfInstallment,
                           Date createdDate,
                           boolean isPaid) {
}
