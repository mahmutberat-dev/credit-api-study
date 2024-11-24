package com.mbi.study.controller.dto;

public record PayLoanRequest(long loanId, long customerId, double amount) {

}
