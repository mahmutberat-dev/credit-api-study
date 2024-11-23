package com.mbi.study.service;

import com.mbi.study.controller.dto.CreateCustomerRequest;
import com.mbi.study.repository.CustomerRepository;
import com.mbi.study.repository.entity.Customer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No any customer found with customerId: %d", customerId)));
    }

    @Override
    public Customer create(CreateCustomerRequest createCustomerRequest) {
        Customer customer = Customer.builder()
                .name(createCustomerRequest.getName())
                .surname(createCustomerRequest.getSurname())
                .creditLimit(createCustomerRequest.getCreditLimit())
                .usedCreditLimit(BigDecimal.ZERO)
                .loans(List.of())
                .build();
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateUsedCreditLimit(Customer customer, BigDecimal totalLoanAmount) {
        customer.addUserCreditLimit(totalLoanAmount);
        return customerRepository.save(customer);
    }

    @Override
    public Customer freeUsedCreditLimit(Customer customer, BigDecimal totalLoanAmount) {
        customer.addUserCreditLimit(totalLoanAmount);
        return customerRepository.save(customer);
    }
}
