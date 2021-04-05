package com.xms.app.massage.service;

import com.xms.app.massage.model.Practitioner;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;

import java.util.List;

public interface PractitionerService {

    void savePractitioner(Practitioner practitioner);

    Page<Practitioner> getPage(PagingRequest pagingRequest);

    Practitioner loadPractitioner(long staffId);

    void deactivatePractitioner(long staffId);

    void deletePractitioner(long staffId);

    List<Practitioner> getAllPractitioners();
}
