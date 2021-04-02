package com.xms.app.massage.service;

import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;

import java.util.List;
import java.util.Optional;

public interface HealthFundService {
    Optional<HealthFund> findById(long id);

    Optional<HealthFund> findByName(String name);

    void saveHealthFund(final HealthFund healthFund);

    Page<HealthFund> getPage(PagingRequest pagingRequest);

    HealthFund loadHealthFund(long healthFundId);

    void deleteHealthFund(long healthFundId);

    List<String> getHealthFunds(String term);
}
