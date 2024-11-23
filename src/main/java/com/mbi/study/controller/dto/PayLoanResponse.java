package com.mbi.study.controller.dto;

import java.math.BigDecimal;

public record PayLoanResponse(int installmentsPaid,
                              BigDecimal amountPaid,
                              boolean isLoanPaid) {
}
