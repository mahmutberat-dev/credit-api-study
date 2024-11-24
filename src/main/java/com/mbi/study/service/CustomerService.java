package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCustomerRequest;
import com.mbi.study.repository.entity.Customer;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public interface CustomerService {

    Customer getById(Long customerId);
    Customer create(CreateCustomerRequest createCustomerRequest);

    Customer updateUsedCreditLimit(Customer customer, BigDecimal totalLoanAmount);
    Customer freeUsedCreditLimit(Customer customer, BigDecimal totalLoanAmount);

    Customer getByUserName(@NotNull String username);
}
