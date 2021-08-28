package com.xms.app.massage.service;

import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.TreatmentVO;

import java.util.Optional;

public interface HomeService {

    Optional<Treatment> findById(long id);
    void saveTreatment(final Treatment treatment);

    Page<ConsultationVO> getPage(PagingRequest pagingRequest);

    void assignCustomerToPractitioner(TreatmentVO treatmentVO);

}
