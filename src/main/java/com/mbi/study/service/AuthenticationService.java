package com.mbi.study.service;

import com.mbi.study.controller.dto.LoginRequest;
import com.mbi.study.controller.dto.LoginResponse;
import com.mbi.study.controller.dto.RegistrationRequest;

public interface AuthenticationService {
    LoginResponse register(RegistrationRequest registrationRequest);

    LoginResponse login(LoginRequest loginRequest);
}
