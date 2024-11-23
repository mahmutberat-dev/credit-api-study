package com.mbi.study.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

public record LoanInstallmentResponse(Long loanId,
                                      Long installmentId,
                                      BigDecimal amount,
                                      BigDecimal paidAmount,
                                      Date nextPaymentDate,
                                      Date paymentDate,
                                      int remainingInstallments) {
}
