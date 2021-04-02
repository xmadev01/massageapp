package com.xms.app.massage.service;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.HealthFundRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class HealthFundServiceImpl implements HealthFundService {

    @Autowired
    private HealthFundRepository healthFundRepository;

    @Override
    public Optional<HealthFund> findById(long id) {
        return healthFundRepository.findById(id);
    }

    @Override
    public Optional<HealthFund> findByName(String name) {
        return healthFundRepository.findByName(name.toLowerCase());
    }

    @Override
    public void saveHealthFund(HealthFund healthFund) {
        healthFundRepository.save(healthFund);
    }

    @Override
    public Page<HealthFund> getPage(PagingRequest pagingRequest) {

        List<HealthFund> healthFunds = new ArrayList<>();

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            healthFunds = healthFundRepository.findAll(Sort.by(Sort.Direction.ASC, column.getData()));
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            healthFunds = healthFundRepository.findAll(Sort.by(Sort.Direction.DESC, column.getData()));
        }
        final List<HealthFund> filteredHealthFunds = healthFunds.stream()
                .filter(filterHealthFund(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());
        long count = healthFunds.stream()
                .filter(filterHealthFund(pagingRequest))
                .count();
        Page<HealthFund> page = new Page<>(filteredHealthFunds);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    private Predicate<HealthFund> filterHealthFund(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return healthFund -> true;
        }

        String value = pagingRequest.getSearch().getValue();

        return healthFund -> healthFund.getName().toLowerCase().contains(value);
    }

    @Override
    public HealthFund loadHealthFund(final long healthFundId) {
        Optional<HealthFund> healthFund = healthFundRepository.findById(healthFundId);
        return healthFund.isPresent() ? healthFund.get() : null;
    }

    @Override
    public void deleteHealthFund(final long healthFundId) {
        healthFundRepository.deleteById(healthFundId);
    }

    @Override
    public List<String> getHealthFunds(String term) {
        return healthFundRepository.findAll().stream()
                .filter(hf -> hf.getName().toLowerCase().contains(term.toLowerCase()))
                .map(HealthFund::getName)
                .collect(Collectors.toList());
    }

}
