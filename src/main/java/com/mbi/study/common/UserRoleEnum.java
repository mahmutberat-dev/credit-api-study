package com.mbi.study.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRoleEnum {
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN");

    private final String value;
}
