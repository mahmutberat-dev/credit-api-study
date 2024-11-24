package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateUserRequest;
import com.mbi.study.repository.UserRepository;
import com.mbi.study.repository.entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(Long customerId) {
        return userRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No any customer found with customerId: %d", customerId)));
    }

    @Override
    public User create(CreateUserRequest createUserRequest) {
        if (userRepository.findByUsername(createUserRequest.username()).isPresent()) {
            throw new EntityExistsException("The user already exists with username %s".formatted(createUserRequest.username()));
        }
        User user = User.builder()
                .name(createUserRequest.name())
                .surname(createUserRequest.surname())
                .creditLimit(createUserRequest.creditLimit())
                .username(createUserRequest.username())
                .password(createUserRequest.password())
                .usedCreditLimit(BigDecimal.ZERO)
                .roleName(createUserRequest.customerRole())
                .loans(List.of())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUsedCreditLimit(User user, BigDecimal totalLoanAmount) {
        user.addUserCreditLimit(totalLoanAmount);
        return userRepository.save(user);
    }

    @Override
    public User freeUsedCreditLimit(User user, BigDecimal totalLoanAmount) {
        user.addUserCreditLimit(totalLoanAmount);
        return userRepository.save(user);
    }

    @Override
    public User getByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No any customer found with username: %s", username)));
    }
}
