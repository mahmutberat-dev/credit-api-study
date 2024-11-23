package com.mbi.study.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateLoanResponse {
    private final BigDecimal monthlyInstallmentAmount;
    private final BigDecimal totalPayment;
    private final BigDecimal totalInterest;
}