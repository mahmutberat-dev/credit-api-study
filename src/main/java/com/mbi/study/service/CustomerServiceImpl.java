package com.mbi.study.service;

import com.mbi.study.repository.CustomerRepository;
import com.mbi.study.repository.entity.Customer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(String.format("No any customer found with customerId: %d", customerId)));
    }
}
