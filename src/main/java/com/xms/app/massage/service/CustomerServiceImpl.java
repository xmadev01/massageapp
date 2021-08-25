package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getCustomers(String term) {
        return customerRepository.findAll().stream()
                                    .filter(cust -> cust.getFullName().toLowerCase().contains(term.toLowerCase()))
                                    .collect(Collectors.toList());
    }

    @Override
    public Page<Customer> getPage(PagingRequest pagingRequest) {

        List<Customer> customers = new ArrayList<>();

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            customers = customerRepository.findAll(Sort.by(Sort.Direction.ASC, column.getData()));
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            customers = customerRepository.findAll(Sort.by(Sort.Direction.DESC, column.getData()));
        }
        final List<Customer> filteredCustomers = customers.stream()
                                                          .filter(customer -> customer.isActive())
                                                          .filter(filterCustomers(pagingRequest))
                                                          .skip(pagingRequest.getStart())
                                                          .limit(pagingRequest.getLength())
                                                          .collect(Collectors.toList());
        long count = customers.stream().filter(customer -> customer.isActive())
                              .filter(filterCustomers(pagingRequest))
                              .count();
        Page<Customer> page = new Page<>(filteredCustomers);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    private Predicate<Customer> filterCustomers(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return customer -> true;
        }

        String value = pagingRequest.getSearch().getValue();

        return customer -> customer.getFirstName().toLowerCase().contains(value)
                || customer.getLastName().toLowerCase().contains(value);
    }

    @Override
    public void saveCustomer(final Customer customer) {
        customerRepository.save(customer);
    }


    @Override
    public Customer loadCustomer(final long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.isPresent() ? customer.get() : null;
    }

    @Override
    public void deactivateCustomer(final long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            customer.get().setActive(!customer.get().isActive());
        }
    }

    @Override
    public void deleteCustomer(final long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Optional<Customer> findByFirstNameLastName(String firstName, String middleName, String lastName) {
       return customerRepository.findByFirstNameLastName(firstName, middleName, lastName);
    }

}
