package com.mbi.study.repository.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCustomerHasEnoughLimit() {
        User user = new User();
        user.setCreditLimit(BigDecimal.valueOf(10_000));
        assertTrue(user.hasEnoughLimit(BigDecimal.valueOf(10_000)));
    }
    @Test
    void shouldCustomerDoesNotHasEnoughLimit() {
        User user = new User();
        user.setCreditLimit(BigDecimal.valueOf(10_000));
        assertFalse(user.hasEnoughLimit(BigDecimal.valueOf(10_001)));
    }
}