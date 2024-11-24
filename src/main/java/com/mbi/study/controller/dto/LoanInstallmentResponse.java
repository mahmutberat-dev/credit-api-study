package com.mbi.study.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

public record LoanInstallmentResponse(Long loanId,
                                      Long id,
                                      BigDecimal amount,
                                      BigDecimal paidAmount,
                                      Date nextPaymentDate,
                                      Date paymentDate,
                                      Boolean isPaid) {
}
