package com.mbi.study.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCustomerRequest {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String surname;
    @NotEmpty
    private final BigDecimal creditLimit;
}
