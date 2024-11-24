package com.mbi.study.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCustomerRequest(
        @NotEmpty String name,
        @NotEmpty String surname,
        @NotEmpty String password,
        @NotNull BigDecimal creditLimit) {
}
