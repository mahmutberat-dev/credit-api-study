package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateUserRequest;
import com.mbi.study.controller.dto.UpdateCreditLimitRequest;
import com.mbi.study.repository.entity.User;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public interface UserService {

    User getById(Long customerId);
    User create(CreateUserRequest createUserRequest);

    User updateCreditLimit(UpdateCreditLimitRequest updateCreditLimitRequest);
    User addUsedCreditLimit(User user, BigDecimal totalLoanAmount);
    User freeUsedCreditLimit(User user, BigDecimal totalLoanAmount);

    User getByUserName(@NotNull String username);

}
