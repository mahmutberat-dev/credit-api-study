package com.mbi.study.controller;

import com.mbi.study.controller.dto.LoginRequest;
import com.mbi.study.controller.dto.LoginResponse;
import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.service.AuthenticationService;
import com.mbi.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
//    @PreAuthorize("registrationRequest.customerRole() == 'ADMIN' AND hasRole('ADMIN')")
    public void register(@RequestBody RegistrationRequest registrationRequest) {
        authenticationService.register(registrationRequest);
    }

}
