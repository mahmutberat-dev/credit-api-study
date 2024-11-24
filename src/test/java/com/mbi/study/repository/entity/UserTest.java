package com.mbi.study.repository.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void shouldCustomerHasEnoughLimit() {
        User user = new User();
        user.setUsedCreditLimit(BigDecimal.ZERO);
        user.setCreditLimit(BigDecimal.valueOf(10_000));
        assertTrue(user.hasEnoughLimit(BigDecimal.valueOf(10_000)));
    }

    @Test
    void shouldCustomerDoesNotHasEnoughLimit() {
        User user = new User();
        user.setUsedCreditLimit(BigDecimal.ZERO);
        user.setCreditLimit(BigDecimal.valueOf(10_000));
        assertFalse(user.hasEnoughLimit(BigDecimal.valueOf(10_001)));
    }

    @Test
    void shouldCustomerDoesNotHasEnoughLimitWithUsedCreditLimit() {
        User user = new User();
        user.setUsedCreditLimit(BigDecimal.valueOf(1_000));
        user.setCreditLimit(BigDecimal.valueOf(10_000));
        assertFalse(user.hasEnoughLimit(BigDecimal.valueOf(9_001)));
    }

    @Test
    void shouldAddLimitCustomerDoesNotHasEnoughLimitWithUsedCreditLimit() {
        User user = new User();
        user.setUsedCreditLimit(BigDecimal.valueOf(1_000));
        user.setCreditLimit(BigDecimal.valueOf(10_000));

        user.addUserCreditLimit(BigDecimal.valueOf(9_000));
        assertTrue(user.getUsedCreditLimit().equals(BigDecimal.valueOf(10_000)));

        assertFalse(user.hasEnoughLimit(BigDecimal.valueOf(1)));
    }
}