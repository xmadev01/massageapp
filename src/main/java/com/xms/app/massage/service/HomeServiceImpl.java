package com.xms.app.massage.service;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.repository.TreatmentRepository;
import com.xms.app.massage.transformer.ConsultationTransformer;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.TreatmentVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HomeServiceImpl extends AbstractXMSService implements HomeService {

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

}
