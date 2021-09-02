package com.xms.app.massage.service;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.utils.CommonUtils;
import com.xms.app.massage.vo.ConsultationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractXMSService {

    protected static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Autowired
    protected EntityManager em;

    protected List<ConsultationVO> populateConsultationVO(List<ConsultationDto> consultationDtos) {
        final List<ConsultationVO> consultations = new ArrayList<>();
        String customer = null;
        String healthFund = null;
        String type = null;
        int countOfSameInsuranceItem = 0;
        int countOfAllInsuranceItem = 0;
        BigDecimal paidAmtCust = BigDecimal.ZERO;
        BigDecimal claimedAmtCust = BigDecimal.ZERO;
        BigDecimal paidAmt = BigDecimal.ZERO;
        BigDecimal claimedAmt = BigDecimal.ZERO;

        for (int i = 0; i < consultationDtos.size(); i ++) {
            final ConsultationDto consultationDto = consultationDtos.get(i);
            final Item item = consultationDto.getItem();
            final BigDecimal pAmt = consultationDto.getPaidAmt();
            final BigDecimal cAmt = consultationDto.getClaimedAmt();
            customer = consultationDto.getCustomerName();
            healthFund = consultationDto.getHealthFund();
            type = consultationDto.getType();

            //check if it is the end of group
            final ConsultationDto nextConsultation = i < consultationDtos.size() - 1 ? consultationDtos.get(i + 1) : null;
            final String nextCustomer = nextConsultation != null ? nextConsultation.getCustomerName() : null;
            final String nextHealthFund = nextConsultation != null ? nextConsultation.getHealthFund() : null;
            final String nextType = nextConsultation != null ? nextConsultation.getType() : null;

            ConsultationVO consultationVo = new ConsultationVO();
            consultationVo.setTreatmentId(consultationDto.getTreatmentId());
            consultationVo.setServiceDate(consultationDto.getServiceDate());
            consultationVo.setCustomerName(customer);
            consultationVo.setItem(item);
            consultationVo.setType(type);
            consultationVo.setHealthFund(healthFund);
            consultationVo.setPaidAmt(CommonUtils.formatCurrencyData(pAmt));
            consultationVo.setClaimedAmt(CommonUtils.formatCurrencyData(cAmt));
            consultationVo.setMedicalCaseRecord(consultationDto.getMedicalCaseRecord());

            paidAmt = paidAmt.add(pAmt);
            claimedAmt = claimedAmt.add(cAmt);
            paidAmtCust = paidAmtCust.add(pAmt);
            claimedAmtCust = claimedAmtCust.add(cAmt);
            consultations.add(consultationVo);
            countOfSameInsuranceItem ++;
            countOfAllInsuranceItem ++;

            if (nextConsultation == null) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                addSubTotalAllInsurance(consultations, countOfAllInsuranceItem, paidAmtCust, claimedAmtCust);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                paidAmtCust = claimedAmtCust = BigDecimal.ZERO;
                countOfSameInsuranceItem = countOfAllInsuranceItem = 0;

            } else if (!nextCustomer.equals(customer)) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                addSubTotalAllInsurance(consultations, countOfAllInsuranceItem, paidAmtCust, claimedAmtCust);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                paidAmtCust = claimedAmtCust = BigDecimal.ZERO;
                countOfSameInsuranceItem = countOfAllInsuranceItem = 0;

            } else if (!ObjectUtils.nullSafeEquals(nextHealthFund, healthFund)) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                countOfSameInsuranceItem = 0;

            } else if (!nextType.equals(type)) {
                addSubTotalIndividualInsurance(consultations, healthFund, countOfSameInsuranceItem, paidAmt, claimedAmt);
                paidAmt = claimedAmt = BigDecimal.ZERO;
                countOfSameInsuranceItem = 0;

            }
        }
        return consultations;
    }

    private void addSubTotalIndividualInsurance(List<ConsultationVO> consultations, String healthFund,
                                                int countOfItem, BigDecimal paidAmt, BigDecimal claimedAmt) {
        ConsultationVO subtotalVo = new ConsultationVO();
        subtotalVo.setHealthFund(Optional.ofNullable(healthFund).orElse("") + " Total(" + countOfItem + "):");
        subtotalVo.setPaidAmt(CommonUtils.formatCurrencyData(paidAmt));
        subtotalVo.setClaimedAmt(CommonUtils.formatCurrencyData(claimedAmt));
        consultations.add(subtotalVo);
    }

    private void addSubTotalAllInsurance(List<ConsultationVO> consultations, int countOfItem,
                                         BigDecimal paidAmtCust, BigDecimal claimedAmtCust) {
        ConsultationVO subtotalVo = new ConsultationVO();
        subtotalVo.setHealthFund("All Insurance Total(" + countOfItem + "):");
        subtotalVo.setPaidAmt(CommonUtils.formatCurrencyData(paidAmtCust));
        subtotalVo.setClaimedAmt(CommonUtils.formatCurrencyData(claimedAmtCust));
        consultations.add(subtotalVo);
    }
}
