package com.mbi.study.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCreditLimitRequest(
        @NotNull Long customerId,
        @Positive long additionalCreditLimit
) {
}
