package com.mbi.study.controller.dto;

import com.mbi.study.common.UserRoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateUserRequest(
        @NotEmpty String name,
        @NotEmpty String surname,
        @NotEmpty String password,
        @NotEmpty String username,
        @NotNull BigDecimal creditLimit,
        @NotNull UserRoleEnum customerRole
        ) {
}
