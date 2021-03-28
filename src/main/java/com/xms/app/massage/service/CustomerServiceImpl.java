package com.xms.app.massage.service;

import com.xms.app.massage.enums.ServiceTypeEnum;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.MassageService;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.CustomerRepository;
import com.xms.app.massage.repository.MassageServiceRepository;
import com.xms.app.massage.vo.ServiceVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private static final Comparator<Customer> EMPTY_COMPARATOR = (c1, c2) -> 0;
    private static final DateTimeFormatter  DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MassageServiceRepository massageServiceRepository;

    @Override
    public List<String> getCustomerNames(String term) {
        return customerRepository.findAll().stream()
                                    .map(Customer::getFullName)
                                    .filter(name -> name.toLowerCase().contains(term.toLowerCase()))
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
                                                          .filter(filterCustomers(pagingRequest))
                                                          .skip(pagingRequest.getStart())
                                                          .limit(pagingRequest.getLength())
                                                          .collect(Collectors.toList());
        long count = customers.stream()
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
                || customer.getLastName().toLowerCase().contains(value)
                || customer.getMembershipNum().toLowerCase().contains(value);
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
    public void assignCustomerToServices(final ServiceVO serviceVO) {
        final String[] names = serviceVO.getCustomerName().split(" ");
        final Optional<Customer> customerOpt = customerRepository.findByFirstNameLastName(names[0], names[1]);
        if (customerOpt.isPresent()) {
            for (ServiceTypeEnum serviceTypeEnum : serviceVO.getServiceType()) {
                final MassageService massageService = new MassageService();
                massageService.setCustomer(customerOpt.get());
                massageService.setServiceDateTime(LocalDate.parse(serviceVO.getServiceDate(), DATE_TIME_FORMATTER).atTime(LocalTime.now()));
                massageService.setServiceType(serviceTypeEnum);
                massageService.setExpenseAmt(BigDecimal.TEN);
                massageService.setClaimedAmt(BigDecimal.TEN);
                massageService.setDuration(60);
                massageServiceRepository.save(massageService);
            }
        }
    }
}
