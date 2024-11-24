package com.mbi.study.controller.dto;

import com.mbi.study.common.UserRoleEnum;

public record RegistrationRequest (String name, String password, UserRoleEnum customerRole){
}
