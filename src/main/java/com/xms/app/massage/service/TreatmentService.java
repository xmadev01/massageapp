package com.xms.app.massage.service;

import com.xms.app.massage.model.Treatment;

import java.util.Optional;

public interface TreatmentService {
    Optional<Treatment> findById(long id);
    void saveTreatment(final Treatment treatment);
}
