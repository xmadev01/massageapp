package com.xms.app.massage.service;

import com.xms.app.massage.model.ServicePrice;

import java.util.List;
import java.util.Optional;

public interface ServicePriceService {
    Optional<ServicePrice> findById(long id);

    List<ServicePrice> getAllServicePrices();

    void saveServicePrice(final ServicePrice servicePrice);
}
