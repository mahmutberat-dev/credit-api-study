package com.mbi.study.repository.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldCustomerHasEnoughLimit() {
        Customer customer = new Customer();
        customer.setCreditLimit(BigDecimal.valueOf(10_000));
        assertTrue(customer.hasEnoughLimit(BigDecimal.valueOf(10_000)));
    }
    @Test
    void shouldCustomerDoesNotHasEnoughLimit() {
        Customer customer = new Customer();
        customer.setCreditLimit(BigDecimal.valueOf(10_000));
        assertFalse(customer.hasEnoughLimit(BigDecimal.valueOf(10_001)));
    }
}