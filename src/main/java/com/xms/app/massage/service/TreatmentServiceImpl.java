package com.xms.app.massage.service;

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
import java.util.function.Predicate;
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

        List<Treatment> treatments = new ArrayList<>();
        List<ConsultationVO> consultations = new ArrayList<>();

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        LocalDate startDate = null, endDate = null;
        String startDateStr = null, endDateStr = null;

        if ("day".equals(pagingRequest.getViewMode())) {
            LocalDate currentDay = pagingRequest.getCurrentDay();
            startDate = endDate = currentDay;
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

        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            if ("customerName".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByCustomerNameAsc(startDate, endDate);
            } else if ("practitionerName".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByPractitionerAsc(startDate, endDate);
            } else if ("item".equals(column.getData())) {
                consultations = findAllOrderByItemAsc(startDateStr, endDateStr);
            } else if ("paidAmt".equals(column.getData())) {
                consultations = findAllOrderByPaidAmtAsc(startDateStr, endDateStr);
            } else if ("claimedAmt".equals(column.getData())) {
                consultations = findAllOrderByClaimedAmtAsc(startDateStr, endDateStr);
            } else if ("serviceDate".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByServiceDateAsc(startDate, endDate);
            }
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            if ("customerName".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByCustomerNameDesc(startDate, endDate);
            } else if ("practitionerName".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByPractitionerDesc(startDate, endDate);
            } else if ("item".equals(column.getData())) {
                consultations = findAllOrderByItemDesc(startDateStr, endDateStr);
            } else if ("paidAmt".equals(column.getData())) {
                consultations = findAllOrderByPaidAmtDesc(startDateStr, endDateStr);
            } else if ("claimedAmt".equals(column.getData())) {
                consultations = findAllOrderByClaimedAmtDesc(startDateStr, endDateStr);
            } else if ("serviceDate".equals(column.getData())) {
                final LocalDate currentDay = pagingRequest.getCurrentDay();
                treatments = treatmentRepository.findAllOrderByServiceDateDesc(startDate, endDate);
            }
        }

        if (consultations.isEmpty()) {
            consultations = populateConsultationVO(treatments);
        }

        final List<ConsultationVO> filteredConsultations = consultations.stream()
                .filter(filterConsultation(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());
        long count = consultations.stream()
                .filter(filterConsultation(pagingRequest))
                .count();
        Page<ConsultationVO> page = new Page<>(filteredConsultations);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    private Predicate<ConsultationVO> filterConsultation(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return consultationVo -> true;
        }

        String value = pagingRequest.getSearch().getValue();

        return consultationVo -> consultationVo.getCustomerName().toLowerCase().startsWith(value.toLowerCase())
                || consultationVo.getServiceDate().startsWith(value.toLowerCase());
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
            final List<Item> items = new ArrayList<>();
            treatmentVO.getItemIds().forEach(itemId -> items.add(itemService.findById(itemId).get()));
            final Treatment treatment = new Treatment();
            treatment.setCustomer(customerOpt.get());
            treatment.setServiceDate(treatmentVO.getServiceDate());
            treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
            treatment.setItems(items);
            treatment.setExpenseAmt(getExpenseAmt(items));
            treatment.setClaimedAmt(getExpenseAmt(items).multiply(BigDecimal.valueOf(customerOpt.get().getRebateRate().doubleValue()))
                    .divide(BigDecimal.valueOf(100)));
            treatment.setCreatedDate(LocalDateTime.now());
            treatmentRepository.save(treatment);
        }
    }

    public List<ConsultationVO> findAllOrderByItemAsc(String startDateStr, String endDateStr) {
        String querySQL = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, t.practitioner, i.price as paidAmt, (i.price * c.rebate_rate / 100) as claimedAmt from treatment t inner join treatment_item ti on t.id = ti.treatment_id ")
                .append("inner join item i on ti.item_id = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("order by i.name asc")
                .toString();
        return em.unwrap(Session.class)
                     .createNativeQuery(querySQL)
                     .setResultTransformer(consultationTransformer).list();

    }

    public List<ConsultationVO> findAllOrderByItemDesc(String startDateStr, String endDateStr) {
        String querySQL = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, t.practitioner, i.price as paidAmt, (i.price * c.rebate_rate / 100) as claimedAmt from treatment t inner join treatment_item ti on t.id = ti.treatment_id ")
                .append("inner join item i on ti.item_id = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("order by i.name desc")
                .toString();
        return em.unwrap(Session.class)
                .createNativeQuery(querySQL)
                .setResultTransformer(consultationTransformer).list();
    }

    public List<ConsultationVO> findAllOrderByPaidAmtAsc(String startDateStr, String endDateStr) {
        String querySQL = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, t.practitioner, i.price as paidAmt, (i.price * c.rebate_rate / 100) as claimedAmt from treatment t inner join treatment_item ti on t.id = ti.treatment_id ")
                .append("inner join item i on ti.item_id = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("order by paidAmt asc")
                .toString();
        return em.unwrap(Session.class)
                .createNativeQuery(querySQL)
                .setResultTransformer(consultationTransformer).list();

    }

    public List<ConsultationVO> findAllOrderByPaidAmtDesc(String startDateStr, String endDateStr) {
        String querySQL = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, t.practitioner, i.price as paidAmt, (i.price * c.rebate_rate / 100) as claimedAmt from treatment t inner join treatment_item ti on t.id = ti.treatment_id ")
                .append("inner join item i on ti.item_id = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("order by paidAmt desc")
                .toString();
        return em.unwrap(Session.class)
                .createNativeQuery(querySQL)
                .setResultTransformer(consultationTransformer).list();

    }

    public List<ConsultationVO> findAllOrderByClaimedAmtAsc(String startDateStr, String endDateStr) {
        String querySQL = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, t.practitioner, i.price as paidAmt, (i.price * c.rebate_rate / 100) as claimedAmt from treatment t inner join treatment_item ti on t.id = ti.treatment_id ")
                .append("inner join item i on ti.item_id = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("order by claimedAmt asc")
                .toString();
        return em.unwrap(Session.class)
                .createNativeQuery(querySQL)
                .setResultTransformer(consultationTransformer).list();

    }

    public List<ConsultationVO> findAllOrderByClaimedAmtDesc(String startDateStr, String endDateStr) {
        String querySQL = new StringBuilder()
                .append("select t.service_date, t.customer, i.id, t.practitioner, i.price as paidAmt, (i.price * c.rebate_rate / 100) as claimedAmt from treatment t inner join treatment_item ti on t.id = ti.treatment_id ")
                .append("inner join item i on ti.item_id = i.id ")
                .append("inner join customer c on t.customer = c.id ")
                .append("where t.service_date between '").append(startDateStr).append("' and '").append(endDateStr).append("' ")
                .append("order by claimedAmt desc")
                .toString();
        return em.unwrap(Session.class)
                .createNativeQuery(querySQL)
                .setResultTransformer(consultationTransformer).list();

    }

    private BigDecimal getExpenseAmt(final List<Item> items) {
        return items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, (subtotal, price) -> subtotal.add(price));

    }

    private List<ConsultationVO> populateConsultationVO(List<Treatment> treatments) {
        final List<ConsultationVO> consultations = new ArrayList<>();
        treatments.forEach(treatment ->
                treatment.getItems().forEach(item -> {
                    ConsultationVO consultationVo = new ConsultationVO();
                    consultationVo.setServiceDate(treatment.getServiceDate().format(dtf));
                    consultationVo.setCustomerName(treatment.getCustomer().getFullName());
                    consultationVo.setItem(item);
                    consultationVo.setPractitionerName(treatment.getPractitioner().getFullName());
                    consultationVo.setPaidAmt(CommonUtils.formatCurrencyData(item.getPrice()));
                    consultationVo.setClaimedAmt(CommonUtils.formatCurrencyData(item.getPrice()
                            .multiply(BigDecimal.valueOf(treatment.getCustomer().getRebateRate()))
                            .divide(BigDecimal.valueOf(100))));
                    consultations.add(consultationVo);
                }));
        return consultations;
    }

}
