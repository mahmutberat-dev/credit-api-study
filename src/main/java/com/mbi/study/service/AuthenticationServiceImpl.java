package com.mbi.study.service;

import com.mbi.study.common.exception.AuthenticationException;
import com.mbi.study.controller.dto.*;
import com.mbi.study.repository.entity.User;
import com.mbi.study.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Override
    public LoginResponse register(RegistrationRequest registrationRequest) {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                registrationRequest.name(),
                registrationRequest.surname(),
                registrationRequest.username(),
                registrationRequest.password(),
                BigDecimal.ZERO,
                registrationRequest.customerRole());
        User user = userService.create(createUserRequest);
        return new LoginResponse(jwtUtil.generateToken(user.getRoleName(), user), user.getId());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        final User user = userService.getByUserName(loginRequest.username());
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new AuthenticationException("Requested login parameters are incorrect!");
        }
        return new LoginResponse(jwtUtil.generateToken(user.getRoleName(), user), user.getId());
    }
}
