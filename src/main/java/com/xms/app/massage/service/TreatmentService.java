package com.xms.app.massage.service;

import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.SingleTreatmentVO;
import com.xms.app.massage.vo.TreatmentVO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TreatmentService {

    Optional<Treatment> findById(long id);
    void saveTreatment(final Treatment treatment);

    Page<ConsultationVO> getPage(PagingRequest pagingRequest);

    void assignCustomerToPractitioner(TreatmentVO treatmentVO);

    void assignCustomerToPractitionerForUpdate(SingleTreatmentVO singleTreatmentVo);

    SingleTreatmentVO loadSingleTreatment(long treatmentId);

    void deactivateTreatment(long treatmentId);

    void downloadInvoice(List<Long> treatmentIds, HttpServletResponse response) throws JRException, IOException;

    boolean validateTreatments(List<Long> treatmentIds, List<ObjectError> errors);
}
