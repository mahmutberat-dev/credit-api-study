package com.mbi.study.controller;

import com.mbi.study.controller.dto.CreateCustomerRequest;
import com.mbi.study.repository.entity.Customer;
import com.mbi.study.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    @PreAuthorize("#customerId == authentication.principal.customerId or hasRole('ADMIN')")
    public Customer getCustomerById(@PathVariable Long customerId) {
        return customerService.getById(customerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Customer create(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerService.create(createCustomerRequest);
    }

}
