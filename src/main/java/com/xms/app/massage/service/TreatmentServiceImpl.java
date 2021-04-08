package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.TreatmentRepository;
import com.xms.app.massage.utils.CommonUtils;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.TreatmentVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            if ("customerName".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByCustomerNameAsc();
            } else if ("practitionerName".equals(column.getData())) {
                treatments = treatmentRepository.findAll(Sort.by(Sort.Direction.ASC, "practitioner"));
            } else {
                treatments = treatmentRepository.findAll(Sort.by(Sort.Direction.ASC, column.getData()));
            }
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            if ("customerName".equals(column.getData())) {
                treatments = treatmentRepository.findAllOrderByCustomerNameDesc();
            } else if ("practitionerName".equals(column.getData())) {
                treatments = treatmentRepository.findAll(Sort.by(Sort.Direction.DESC, "practitioner"));
            } else {
                treatments = treatmentRepository.findAll(Sort.by(Sort.Direction.DESC, column.getData()));
            }
        }

        final List<ConsultationVO> consultations = new ArrayList<>();
        treatments.forEach(treatment -> {
            treatment.getItems().forEach(item -> {
                ConsultationVO consultationVo = new ConsultationVO();
                consultationVo.setServiceDate(treatment.getServiceDate().format(dtf));
                consultationVo.setCustomerName(treatment.getCustomer().getFullName());
                consultationVo.setItem(item);
                consultationVo.setPaidAmt(CommonUtils.formatCurrencyData(item.getPrice()));
                consultationVo.setClaimedAmt(CommonUtils.formatCurrencyData(item.getPrice()
                                                        .multiply(BigDecimal.valueOf(treatment.getCustomer().getRebateRate()))
                                                        .divide(BigDecimal.valueOf(100))));
                consultations.add(consultationVo);
            });

        });

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

        return consultationVo -> consultationVo.getCustomerName().toLowerCase().contains(value.toLowerCase())
                || consultationVo.getServiceDate().contains(value.toLowerCase());
    }

    @Override
    public void assignCustomerToPractitioner(final TreatmentVO treatmentVO) {
        final String[] names = treatmentVO.getCustomerName().split(" ");
        final Optional<Customer> customerOpt = customerService.findByFirstNameLastName(names[0], names[1], names[2]);
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

    private BigDecimal getExpenseAmt(final List<Item> items) {
        return items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, (subtotal, price) -> subtotal.add(price));

    }

}
