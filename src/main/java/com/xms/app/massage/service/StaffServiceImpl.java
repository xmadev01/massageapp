package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Staff;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.StaffRepository;
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
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public void saveStaff(Staff staff) {
        staffRepository.save(staff);
    }

    @Override
    public Page<Staff> getPage(PagingRequest pagingRequest) {

        List<Staff> staff = new ArrayList<>();

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            staff = staffRepository.findAll(Sort.by(Sort.Direction.ASC, column.getData()));
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            staff = staffRepository.findAll(Sort.by(Sort.Direction.DESC, column.getData()));
        }
        final List<Staff> filteredStaff = staff.stream()
                                               .filter(filterStaff(pagingRequest))
                                               .skip(pagingRequest.getStart())
                                               .limit(pagingRequest.getLength())
                                               .collect(Collectors.toList());
        long count = staff.stream()
                          .filter(filterStaff(pagingRequest))
                          .count();
        Page<Staff> page = new Page<>(filteredStaff);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    private Predicate<Staff> filterStaff(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return staff -> true;
        }

        String value = pagingRequest.getSearch().getValue();

        return staff -> staff.getFirstName().toLowerCase().contains(value)
                || staff.getLastName().toLowerCase().contains(value);
    }

    @Override
    public Staff loadStaff(final long staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        return staff.isPresent() ? staff.get() : null;
    }

    @Override
    public void deactivateStaff(final long staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            staff.get().setActive(!staff.get().isActive());
        }
    }

    @Override
    public void deleteStaff(final long staffId) {
        staffRepository.deleteById(staffId);
    }

    @Override
    public List<String> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(Staff::getFullName)
                .collect(Collectors.toList());
    }
}
