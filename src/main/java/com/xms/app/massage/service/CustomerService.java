package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findById(long id);
    void saveCustomer(final Customer customer);
}
