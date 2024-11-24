package com.mbi.study.controller;

import com.mbi.study.common.mapper.UserMapper;
import com.mbi.study.controller.dto.CreateUserRequest;
import com.mbi.study.controller.dto.UpdateCreditLimitRequest;
import com.mbi.study.controller.dto.UserResponse;
import com.mbi.study.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{customerId}")
    @PreAuthorize("#customerId == authentication.principal.id or hasAuthority('ADMIN')")
    public UserResponse getCustomerById(@PathVariable Long customerId) {
        return userMapper.toUserResponse(userService.getById(customerId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userMapper.toUserResponse(userService.create(createUserRequest));
    }

    @PutMapping("/credit-limit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse updateUserCreditLimit(@Valid @RequestBody UpdateCreditLimitRequest updateCreditLimitRequest) {
        return userMapper.toUserResponse(userService.updateCreditLimit(updateCreditLimitRequest));
    }

}
