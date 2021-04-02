package com.xms.app.massage.service;

import com.xms.app.massage.model.ServicePrice;
import com.xms.app.massage.repository.ServicePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicePriceServiceImpl implements ServicePriceService {

    @Autowired
    private ServicePriceRepository servicePriceRepository;

    @Override
    public Optional<ServicePrice> findById(long id) {
        return servicePriceRepository.findById(id);
    }

    @Override
    public List<ServicePrice> getAllServicePrices() {
        return servicePriceRepository.findAll();
    }

    @Override
    public void saveServicePrice(ServicePrice servicePrice) {
        servicePriceRepository.save(servicePrice);
    }
}
