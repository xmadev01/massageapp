package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;

public interface CustomerService {

    Page<Customer> getPage(PagingRequest pagingRequest);

    void saveCustomer(final Customer customer);

    Customer loadCustomer(long customerId);

    void deactivateCustomer(long customerId);

    void deleteCustomer(long customerId);
}
