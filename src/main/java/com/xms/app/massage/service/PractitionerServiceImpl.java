package com.xms.app.massage.service;

import com.xms.app.massage.model.Practitioner;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.PractitionerRepository;
import org.apache.commons.lang3.StringUtils;
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
public class PractitionerServiceImpl implements PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Override
    public void savePractitioner(Practitioner practitioner) {
        practitionerRepository.save(practitioner);
    }

    @Override
    public Page<Practitioner> getPage(PagingRequest pagingRequest) {

        List<Practitioner> practitioners = new ArrayList<>();

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            practitioners = practitionerRepository.findAll(Sort.by(Sort.Direction.ASC, column.getData()));
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            practitioners = practitionerRepository.findAll(Sort.by(Sort.Direction.DESC, column.getData()));
        }
        final List<Practitioner> filteredPractitioner = practitioners.stream()
                                               .filter(filterPractitioner(pagingRequest))
                                               .skip(pagingRequest.getStart())
                                               .limit(pagingRequest.getLength())
                                               .collect(Collectors.toList());
        long count = practitioners.stream()
                          .filter(filterPractitioner(pagingRequest))
                          .count();
        Page<Practitioner> page = new Page<>(filteredPractitioner);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    private Predicate<Practitioner> filterPractitioner(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return practitioner -> true;
        }

        String value = pagingRequest.getSearch().getValue();

        return practitioner -> practitioner.getFirstName().toLowerCase().contains(value)
                || practitioner.getLastName().toLowerCase().contains(value);
    }

    @Override
    public Practitioner loadPractitioner(final long staffId) {
        Optional<Practitioner> staff = practitionerRepository.findById(staffId);
        return staff.isPresent() ? staff.get() : null;
    }

    @Override
    public void deactivatePractitioner(final long staffId) {
        Optional<Practitioner> staff = practitionerRepository.findById(staffId);
        if (staff.isPresent()) {
            staff.get().setActive(!staff.get().isActive());
        }
    }

    @Override
    public void deletePractitioner(final long staffId) {
        practitionerRepository.deleteById(staffId);
    }

    @Override
    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll().stream()
                                               .filter(p -> p.isActive())
                                               .collect(Collectors.toList());
    }
}
