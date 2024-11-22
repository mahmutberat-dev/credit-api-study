package com.mbi.study.service;

import com.mbi.study.repository.entity.Customer;

public interface CustomerService {

    Customer getById(Long customerId);
}
