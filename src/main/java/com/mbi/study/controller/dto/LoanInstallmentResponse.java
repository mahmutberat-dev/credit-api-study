package com.mbi.study.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LoanInstallmentResponse {
    private Long loanId;
    private Long installmentId;
    private BigDecimal amount;
    private Date nextPaymentDate;
    private int remainingInstallments;
}
