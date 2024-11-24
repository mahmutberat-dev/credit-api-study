package com.mbi.study.service;

import com.mbi.study.common.exception.AuthenticationException;
import com.mbi.study.controller.dto.CreateCustomerRequest;
import com.mbi.study.controller.dto.LoginRequest;
import com.mbi.study.controller.dto.LoginResponse;
import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.repository.entity.User;
import com.mbi.study.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.mbi.study.common.UserRoleEnum.ADMIN;
import static com.mbi.study.common.UserRoleEnum.CUSTOMER;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerService customerService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Override
    public void register(RegistrationRequest registrationRequest) {
        if (CUSTOMER.equals(registrationRequest.customerRole())) {
            customerService.create(new CreateCustomerRequest(registrationRequest.name(), registrationRequest.password(), registrationRequest.surname(), BigDecimal.ZERO));
        } else if (ADMIN.equals(registrationRequest.customerRole())) {
            userService.register(registrationRequest);
        } else {
            throw new IllegalArgumentException("The register API can only create ADMIN or CUSTOMER users!");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        final User user = userService.findByUserName(loginRequest.username());
        if (user != null) {
            if (!user.getPassword().equals(loginRequest.password())) {
                throw new AuthenticationException("Password is incorrect");
            }
            return new LoginResponse(jwtUtil.generateToken(user.getRoleName(), user), user.getId());
        }
        customerService.getByUserName(loginRequest.username());
        return null;
    }
}
