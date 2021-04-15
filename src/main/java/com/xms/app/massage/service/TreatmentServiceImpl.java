package com.xms.app.massage.service;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.TreatmentRepository;
import com.xms.app.massage.transformer.ConsultationTransformer;
import com.xms.app.massage.utils.CommonUtils;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.TreatmentVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TreatmentServiceImpl implements TreatmentService {

    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PractitionerService practitionerService;
    @Autowired
    private ConsultationTransformer consultationTransformer;
    @Autowired
    private EntityManager em;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Optional<Treatment> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void saveTreatment(Treatment treatment) {
        treatmentRepository.save(treatment);
    }

    @Override
    public Page<ConsultationVO> getPage(PagingRequest pagingRequest) {

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        LocalDate startDate = null, endDate = null;
        String startDateStr = null, endDateStr = null;

        if ("day".equals(pagingRequest.getViewMode())) {
            LocalDate currentDay = pagingRequest.getCurrentDay();
            startDateStr = endDateStr = currentDay.format(dtf1);
        } else if ("month".equals(pagingRequest.getViewMode())) {
            int currentMonth = Integer.parseInt(pagingRequest.getCurrentMonth());
            int currentYear = Integer.parseInt(pagingRequest.getCurrentYear());
            startDate = LocalDate.of(currentYear, currentMonth, 1);
            endDate = LocalDate.of(currentYear, currentMonth, 1).plusMonths(1).minusDays(1);
            startDateStr = startDate.format(dtf1);
            endDateStr = endDate.format(dtf1);
        } else if ("year".equals(pagingRequest.getViewMode())) {
            int currentYear = Integer.parseInt(pagingRequest.getCurrentYear());
            startDate = LocalDate.of(currentYear, 1, 1);
            endDate = LocalDate.of(currentYear,12, 31);
            startDateStr = startDate.format(dtf1);
            endDateStr = endDate.format(dtf1);
        }

        List<ConsultationDto> consultationDtos = findAllOrderByCustomer(startDateStr, endDateStr, pagingRequest.getSearch().getValue());
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

    @Override
    public void assignCustomerToPractitioner(final TreatmentVO treatmentVO) {
        final String[] names = treatmentVO.getCustomerName().split(" ");
        Optional<Customer> customerOpt;
        if (names.length == 3) {
            customerOpt = customerService.findByFirstNameLastName(names[0], names[1], names[2]);
        } else {
            customerOpt = customerService.findByFirstNameLastName(names[0], "", names[1]);
        }
        if (customerOpt.isPresent()) {
            treatmentVO.getItemIds().forEach(itemId -> {
                final Treatment treatment = new Treatment();
                final Item item = itemService.findById(itemId).get();
                treatment.setCustomer(customerOpt.get());
                treatment.setServiceDate(treatmentVO.getServiceDate());
                treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
                treatment.setItem(item);
                treatment.setExpenseAmt(item.getPrice());
                treatment.setClaimedAmt(item.getPrice().multiply(BigDecimal.valueOf(customerOpt.get().getRebateRate().doubleValue()))
                        .divide(BigDecimal.valueOf(100)));
                treatment.setCreatedDate(LocalDateTime.now());
                treatmentRepository.save(treatment);
            });
        }
    }

    public List<ConsultationDto> findAllOrderByCustomer(String startDateStr, String endDateStr, String searchVal) {
        StringBuilder querySQLBuilder = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, c.health_fund, t.expense_amt, t.claimed_amt from treatment t ")
                .append("inner join item i on t.item = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ");
                if (StringUtils.isNotBlank(searchVal)) {
                    querySQLBuilder.append("and LOWER(CONCAT(c.first_name, c.middle_name, c.last_name)) like '%" + searchVal.toLowerCase() + "%' ");
                }
                querySQLBuilder.append("order by c.first_name asc, c.middle_name asc, c.last_name asc, i.type asc, t.service_date asc, i.name asc");

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

                } else if (!nextHealthFund.equals(healthFund)) {
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
        subtotalVo.setHealthFund(healthFund + " Total(" + countOfItem + "):");
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
