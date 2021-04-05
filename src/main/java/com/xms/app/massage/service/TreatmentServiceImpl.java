package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.repository.TreatmentRepository;
import com.xms.app.massage.vo.TreatmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Override
    public Optional<Treatment> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void saveTreatment(Treatment treatment) {
        treatmentRepository.save(treatment);
    }

    @Override
    public void assignCustomerToPractitioner(final TreatmentVO treatmentVO) {
        final String[] names = treatmentVO.getCustomerName().split(" ");
        final Optional<Customer> customerOpt = customerService.findByFirstNameLastName(names[0], names[1], names[2]);
        if (customerOpt.isPresent()) {
            final List<Item> items = treatmentVO.getItemIds().stream()
                    .map(itemId -> itemService.findById(itemId).get())
                    .collect(Collectors.toList());
            final Treatment treatment = new Treatment();
            treatment.setCustomer(customerOpt.get());
            treatment.setServiceDate(treatmentVO.getServiceDate());
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
