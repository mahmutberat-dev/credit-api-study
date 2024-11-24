package com.mbi.study.controller.dto;

import com.mbi.study.common.UserRoleEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull @Max(64) String name,
        @NotNull @Max(32) String surname,
        @NotNull @Max(16) String password,
        @NotNull UserRoleEnum customerRole
) {
}
