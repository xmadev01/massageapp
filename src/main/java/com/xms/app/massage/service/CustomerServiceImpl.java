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
import java.time.format.DateTimeFormatter;
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

        List<ConsultationDto> consultationDtos = findAllTreatments(pagingRequest);
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

    public List<ConsultationDto> findAllTreatments(PagingRequest pagingRequest) {
        StringBuilder querySQLBuilder = new StringBuilder()
                .append("select t.id as treatmentId, t.service_date, t.customer, i.id as itemId, c.health_fund, t.expense_amt, t.claimed_amt, t.medical_case_record from treatment t ")
                .append("inner join item i on t.item = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where c.id = " + pagingRequest.getCustomerId() + " ");
        if (StringUtils.isNotBlank(pagingRequest.getFromDate()) && StringUtils.isNotBlank(pagingRequest.getToDate())) {
            LocalDate startDate = LocalDate.parse(pagingRequest.getFromDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(pagingRequest.getToDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1);
            querySQLBuilder.append("and t.service_date >= '").append(startDate.format(dtf)).append("' ");
            querySQLBuilder.append("and t.service_date < '").append(endDate.format(dtf)).append("' ");
        } else if (StringUtils.isNotBlank(pagingRequest.getFromDate())) {
            LocalDate startDate = LocalDate.parse(pagingRequest.getFromDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            querySQLBuilder.append("and t.service_date >= '").append(startDate.format(dtf)).append("' ");
        } else if (StringUtils.isNotBlank(pagingRequest.getToDate())) {
            LocalDate endDate = LocalDate.parse(pagingRequest.getToDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1);
            querySQLBuilder.append("and t.service_date < '").append(endDate.format(dtf)).append("' ");
        }
        querySQLBuilder.append("and t.active = 1 ");
        querySQLBuilder.append("order by c.first_name asc, c.middle_name asc, c.last_name asc, i.type asc, t.service_date asc, i.name asc");

        return em.unwrap(Session.class)
                .createNativeQuery(querySQLBuilder.toString())
                .setResultTransformer(consultationTransformer).list();

    }

}
