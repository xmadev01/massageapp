package com.xms.app.massage.service;

import com.xms.app.massage.model.MassageService;
import com.xms.app.massage.repository.MassageServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MassageServiceServiceImpl implements MassageServiceService {

    @Autowired
    private MassageServiceRepository massageServiceRepository;

    @Override
    public Optional<MassageService> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void saveMassageService(MassageService massageService) {
        massageServiceRepository.save(massageService);
    }
}
