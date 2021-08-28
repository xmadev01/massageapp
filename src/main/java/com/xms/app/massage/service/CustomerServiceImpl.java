package com.xms.app.massage.service;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.CustomerRepository;
import com.xms.app.massage.transformer.ConsultationTransformer;
import com.xms.app.massage.utils.CommonUtils;
import com.xms.app.massage.vo.ConsultationVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    private List<ConsultationVO> populateConsultationVO(List<ConsultationDto> consultationDtos) {
        final List<ConsultationVO> consultations = new ArrayList<>();
        String customer = null;
        String healthFund = null;
        String type = null;
        int countOfSameInsuranceItem = 0;
        int countOfAllInsuranceItem = 0;
        BigDecimal paidAmtCust = BigDecimal.ZERO;
        BigDecimal claimedAmtCust = BigDecimal.ZERO;
        BigDecimal paidAmt = BigDecimal.ZERO;
        BigDecimal claimedAmt = BigDecimal.ZERO;

        for (int i = 0; i < consultationDtos.size(); i ++) {
            final ConsultationDto consultationDto = consultationDtos.get(i);
            final Item item = consultationDto.getItem();
            final BigDecimal pAmt = consultationDto.getPaidAmt();
            final BigDecimal cAmt = consultationDto.getClaimedAmt();
            customer = consultationDto.getCustomerName();
            healthFund = consultationDto.getHealthFund();
            type = consultationDto.getType();

            //check if it is the end of group
            final ConsultationDto nextConsultation = i < consultationDtos.size() - 1 ? consultationDtos.get(i + 1) : null;
            final String nextCustomer = nextConsultation != null ? nextConsultation.getCustomerName() : null;
            final String nextHealthFund = nextConsultation != null ? nextConsultation.getHealthFund() : null;
            final String nextType = nextConsultation != null ? nextConsultation.getType() : null;

            ConsultationVO consultationVo = new ConsultationVO();
            consultationVo.setServiceDate(consultationDto.getServiceDate());
            consultationVo.setCustomerName(customer);
            consultationVo.setItem(item);
            consultationVo.setType(type);
            consultationVo.setHealthFund(healthFund);
            consultationVo.setPaidAmt(CommonUtils.formatCurrencyData(pAmt));
            consultationVo.setClaimedAmt(CommonUtils.formatCurrencyData(cAmt));

            paidAmt = paidAmt.add(pAmt);
            claimedAmt = claimedAmt.add(cAmt);
            paidAmtCust = paidAmtCust.add(pAmt);
            claimedAmtCust = claimedAmtCust.add(cAmt);
            consultations.add(consultationVo);
            countOfSameInsuranceItem ++;
            countOfAllInsuranceItem ++;

            if (nextConsultation == null) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                addSubTotalAllInsurance(consultations, countOfAllInsuranceItem, paidAmtCust, claimedAmtCust);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                paidAmtCust = claimedAmtCust = BigDecimal.ZERO;
                countOfSameInsuranceItem = countOfAllInsuranceItem = 0;

            } else if (!nextCustomer.equals(customer)) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                addSubTotalAllInsurance(consultations, countOfAllInsuranceItem, paidAmtCust, claimedAmtCust);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                paidAmtCust = claimedAmtCust = BigDecimal.ZERO;
                countOfSameInsuranceItem = countOfAllInsuranceItem = 0;

            } else if (!ObjectUtils.nullSafeEquals(nextHealthFund, healthFund)) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                countOfSameInsuranceItem = 0;

            } else if (!nextType.equals(type)) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                countOfSameInsuranceItem = 0;

            }
        }
        return consultations;
    }

    private void addSubTotalIndividualInsurance(List<ConsultationVO> consultations, String healthFund,
                                                int countOfItem, BigDecimal paidAmt, BigDecimal claimedAmt) {
        ConsultationVO subtotalVo = new ConsultationVO();
        subtotalVo.setHealthFund(Optional.ofNullable(healthFund).orElse("") + " Total(" + countOfItem + "):");
        subtotalVo.setPaidAmt(CommonUtils.formatCurrencyData(paidAmt));
        subtotalVo.setClaimedAmt(CommonUtils.formatCurrencyData(claimedAmt));
        consultations.add(subtotalVo);
    }

    private void addSubTotalAllInsurance(List<ConsultationVO> consultations, int countOfItem,
                                         BigDecimal paidAmtCust, BigDecimal claimedAmtCust) {
        ConsultationVO subtotalVo = new ConsultationVO();
        subtotalVo.setHealthFund("All Insurance Total(" + countOfItem + "):");
        subtotalVo.setPaidAmt(CommonUtils.formatCurrencyData(paidAmtCust));
        subtotalVo.setClaimedAmt(CommonUtils.formatCurrencyData(claimedAmtCust));
        consultations.add(subtotalVo);
    }

}
