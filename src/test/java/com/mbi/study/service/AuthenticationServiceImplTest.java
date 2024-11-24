package com.mbi.study.service;

import com.mbi.study.common.UserRoleEnum;
import com.mbi.study.common.exception.AuthenticationException;
import com.mbi.study.controller.dto.CreateUserRequest;
import com.mbi.study.controller.dto.LoginRequest;
import com.mbi.study.controller.dto.LoginResponse;
import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.repository.entity.User;
import com.mbi.study.security.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Hugo",
                "Lee",
                "pass",
                "user-1",
                UserRoleEnum.CUSTOMER);
        CreateUserRequest userRequest = new CreateUserRequest(
                "Hugo",
                "Lee",
                "pass",
                "user-1",
                BigDecimal.ZERO,
                UserRoleEnum.CUSTOMER);

        User user = new User();
        user.setId(1001L);
        user.setRoleName(UserRoleEnum.CUSTOMER);
        when(userService.create(userRequest)).thenReturn(user);

        when(jwtUtil.generateToken(UserRoleEnum.CUSTOMER, user)).thenReturn("tokenValue");

        LoginResponse loginResponse = authenticationService.register(registrationRequest);

        assertEquals("tokenValue", loginResponse.token());
        assertEquals(user.getUserId(), loginResponse.userId());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("user-1", "pass");

        User user = User.builder().id(1001L).username("user-1").password("pass").roleName(UserRoleEnum.CUSTOMER).build();
        when(userService.getByUserName(loginRequest.username())).thenReturn(user);

        when(jwtUtil.generateToken(UserRoleEnum.CUSTOMER, user)).thenReturn("tokenValue");

        LoginResponse loginResponse = authenticationService.login(loginRequest);

        assertEquals("tokenValue", loginResponse.token());
        assertEquals(user.getUserId(), loginResponse.userId());
    }

    @Test
    void shouldThrowExceptionForIncorrectLoginRequest() {
        LoginRequest loginRequest = new LoginRequest("user-1", "pass");

        User user = User.builder().id(1001L).username("user-1").password("pass-new").roleName(UserRoleEnum.CUSTOMER).build();
        when(userService.getByUserName(loginRequest.username())).thenReturn(user);

        assertThrows(AuthenticationException.class, () -> authenticationService.login(loginRequest));
    }
}
