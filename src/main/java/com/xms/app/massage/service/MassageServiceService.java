package com.xms.app.massage.service;

import com.xms.app.massage.model.MassageService;

import java.util.Optional;

public interface MassageServiceService {
    Optional<MassageService> findById(long id);
    void saveMassageService(final MassageService massageService);
}
