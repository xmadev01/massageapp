package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.vo.ConsultationVO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> getCustomers(String term);

    Page<Customer> getPage(PagingRequest pagingRequest);

    void saveCustomer(final Customer customer);

    Customer loadCustomer(long customerId);

    void deactivateCustomer(long customerId);

    void deleteCustomer(long customerId);

    Optional<Customer> findByFirstNameLastName(String firstName, String middleName, String lastName);

    Page<ConsultationVO> loadCustomerTreatments(PagingRequest pagingRequest);
}