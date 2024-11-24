package com.mbi.study.service;

import com.mbi.study.controller.dto.LoginRequest;
import com.mbi.study.controller.dto.LoginResponse;
import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.repository.entity.User;

public interface AuthenticationService {
    void register(RegistrationRequest registrationRequest);

    LoginResponse login(LoginRequest loginRequest);
}
