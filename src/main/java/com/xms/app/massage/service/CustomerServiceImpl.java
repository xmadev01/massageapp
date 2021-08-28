package com.xms.app.massage.service;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.CustomerRepository;
import com.xms.app.massage.transformer.ConsultationTransformer;
import com.xms.app.massage.vo.ConsultationVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl extends AbstractXMSService implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private ConsultationTransformer consultationTransformer;

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

    @Override
    public Page<ConsultationVO> loadCustomerTreatments(PagingRequest pagingRequest) {
        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        LocalDate startDate = null, endDate = null;
        String startDateStr = null, endDateStr = null;

        if ("day".equals(pagingRequest.getViewMode())) {
            LocalDate currentDay = pagingRequest.getCurrentDay();
            startDateStr = endDateStr = currentDay.format(dtf);
        } else if ("month".equals(pagingRequest.getViewMode())) {
            int currentMonth = Integer.parseInt(pagingRequest.getCurrentMonth());
            int currentYear = Integer.parseInt(pagingRequest.getCurrentYear());
            startDate = LocalDate.of(currentYear, currentMonth, 1);
            endDate = LocalDate.of(currentYear, currentMonth, 1).plusMonths(1).minusDays(1);
            startDateStr = startDate.format(dtf);
            endDateStr = endDate.format(dtf);
        } else if ("year".equals(pagingRequest.getViewMode())) {
            int currentYear = Integer.parseInt(pagingRequest.getCurrentYear());
            startDate = LocalDate.of(currentYear, 1, 1);
            endDate = LocalDate.of(currentYear,12, 31);
            startDateStr = startDate.format(dtf);
            endDateStr = endDate.format(dtf);
        }

        List<ConsultationDto> consultationDtos = findAllTreatments(startDateStr, endDateStr, pagingRequest.getCustomerId());
        List<ConsultationVO> consultations = new ArrayList<>();

        if (!consultationDtos.isEmpty()) {
            consultations = populateConsultationVO(consultationDtos);
        }

        final List<ConsultationVO> filteredConsultations = consultations.stream()
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());
        long count = consultations.stream()
                .count();
        Page<ConsultationVO> page = new Page<>(filteredConsultations);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    public List<ConsultationDto> findAllTreatments(String startDateStr, String endDateStr, long customerId) {
        StringBuilder querySQLBuilder = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, c.health_fund, t.expense_amt, t.claimed_amt from treatment t ")
                .append("inner join item i on t.item = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("and c.id = " + customerId + " ")
                .append("order by c.first_name asc, c.middle_name asc, c.last_name asc, i.type asc, t.service_date asc, i.name asc");

        return em.unwrap(Session.class)
                .createNativeQuery(querySQLBuilder.toString())
                .setResultTransformer(consultationTransformer).list();

    }

}
