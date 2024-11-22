package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCustomerRequest;
import com.mbi.study.repository.CustomerRepository;
import com.mbi.study.repository.entity.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldGetById() {
        long customerId = 1001L;
        Customer customer = Customer.builder().id(customerId).name("John").surname("Sir").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer result = customerService.getById(customerId);
        assertEquals(customer, result);
    }

    @Test
    void shouldThrowEntityNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerService.getById(1001L));
    }

    @Test
    void shouldCreateCustomer() {
        when(customerRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Customer customer = customerService.create(new CreateCustomerRequest("John", "Sir", BigDecimal.valueOf(100_000)));

        assertEquals("John", customer.getName());
        assertEquals("Sir", customer.getSurname());
        assertEquals(BigDecimal.valueOf(100_000), customer.getCreditLimit());
    }
}