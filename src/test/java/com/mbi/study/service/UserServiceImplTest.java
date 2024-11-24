package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateUserRequest;
import com.mbi.study.repository.UserRepository;
import com.mbi.study.repository.entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mbi.study.common.UserRoleEnum.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetById() {
        long customerId = 1001L;
        User user = User.builder().id(customerId).name("John").surname("Sir").build();
        when(userRepository.findById(customerId)).thenReturn(Optional.of(user));
        User result = userService.getById(customerId);
        assertEquals(user, result);
    }

    @Test
    void shouldThrowEntityNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getById(1001L));
    }

    @Test
    void shouldCreateCustomer() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User user = userService.create(new CreateUserRequest("John", "Sir", "pass", "user-1", BigDecimal.valueOf(100_000), CUSTOMER));

        assertEquals("John", user.getName());
        assertEquals("Sir", user.getSurname());
        assertEquals("pass", user.getPassword());
        assertEquals("user-1", user.getUserName());
        assertEquals(CUSTOMER, user.getRoleName());
        assertEquals(BigDecimal.valueOf(100_000), user.getCreditLimit());
    }

    @Test
    void shouldThrowExceptionForSameUsernameNotUnique() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(User.builder().build()));

        assertThrows(EntityExistsException.class, () -> userService.create(new CreateUserRequest("John", "Sir", "user-1", "pass", BigDecimal.valueOf(100_000), CUSTOMER)));
    }

    @Test
    void shouldGetByUserName() {
        String userName = "user-1";
        User user = User.builder().id(1001L).name("John").surname("Sir").username(userName).build();
        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        User result = userService.getByUserName(userName);
        assertEquals(user, result);
    }

    @Test
    void shouldThrowEntityNotFoundForGetByUserName() {
        String userName = "user-1";
        when(userRepository.findByUsername(userName)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getByUserName(userName));
    }

    @Test
    void shouldUpdateUsedCreditLimit() {
        User user = User.builder().id(1001L).name("John").surname("Sir").creditLimit(BigDecimal.ZERO).usedCreditLimit(BigDecimal.ZERO).build();
        BigDecimal totalLoanAmount = BigDecimal.valueOf(50000);
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.updateUsedCreditLimit(user, totalLoanAmount);

        assertEquals(result.getUsedCreditLimit(), totalLoanAmount);
    }

}
