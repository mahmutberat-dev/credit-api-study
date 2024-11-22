package com.mbi.study.controller.dto;

import com.mbi.study.validation.OneOf;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCreditLoanRequest {
    @Positive
    private Long customerId;

    @Positive
    private BigDecimal amount;

    @DecimalMin(value = "0.1")
    @DecimalMax(value = "0.5")
    @Digits(integer = 3, fraction = 2)
    private double interestRate;

    @OneOf(value = {6, 9, 12, 24})
    private int numberOfInstallments;
}
