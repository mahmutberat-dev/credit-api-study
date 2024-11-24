package com.mbi.study.controller;

import com.mbi.study.controller.dto.CreateUserRequest;
import com.mbi.study.repository.entity.User;
import com.mbi.study.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final UserService userService;

    @GetMapping("/{customerId}")
    @PreAuthorize("#customerId == authentication.principal.customerId or hasRole('ADMIN')")
    public User getCustomerById(@PathVariable Long customerId) {
        return userService.getById(customerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest);
    }

}
