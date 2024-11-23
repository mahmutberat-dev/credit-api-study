package com.mbi.study.controller.dto;

import lombok.Data;

@Data
public class PayLoanRequest {
    private long loanId;
    private long customerId;
    private double amount;
}
