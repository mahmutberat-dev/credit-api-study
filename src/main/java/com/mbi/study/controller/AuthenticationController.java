package com.mbi.study.controller;

import com.mbi.study.controller.dto.RegistrationRequest;
import com.mbi.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequest registrationRequest) {
        userService.register(registrationRequest);
    }

}
