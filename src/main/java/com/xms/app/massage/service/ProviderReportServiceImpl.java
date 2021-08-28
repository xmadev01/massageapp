package com.xms.app.massage.service;

import com.xms.app.massage.enums.ServiceTypeEnum;
import com.xms.app.massage.model.*;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.repository.TreatmentRepository;
import com.xms.app.massage.utils.CommonUtils;
import com.xms.app.massage.vo.ProviderReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProviderReportServiceImpl extends AbstractXMSService implements ProviderReportService {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Override
    public Page<ProviderReportVO> getPage(PagingRequest pagingRequest) {
        LocalDate fromDate = null;
        LocalDate toDate = null;

        if(StringUtils.isNotBlank(pagingRequest.getFromDate()) && StringUtils.isNotBlank(pagingRequest.getToDate())) {
            fromDate = LocalDate.parse(pagingRequest.getFromDate(), dtf1);
            toDate = LocalDate.parse(pagingRequest.getToDate(), dtf1);
        }
        List<ProviderReportVO> reports = populateProviderReportVO(fromDate, toDate);
        reports = reports.stream().filter(re -> re.getNumberOfAcupunctureClient() != 0 || re.getNumberOfMassageClient() != 0).collect(Collectors.toList());

        final List<ProviderReportVO> filteredConsultations = reports.stream()
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = reports.stream()
                .count();
        Page<ProviderReportVO> page = new Page<>(filteredConsultations);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    private List<ProviderReportVO> populateProviderReportVO(LocalDate startDate, LocalDate endDate) {
        List<ProviderReportVO> reportVos = new ArrayList<>();

        if (startDate != null && endDate != null) {
            for (LocalDate sDate = startDate; sDate.isBefore(endDate.plusDays(1)); sDate = sDate.plusDays(1)) {
                for (Practitioner practitioner : treatmentRepository.findAllProviders(sDate)) {
                    for (HealthFund healthFund : treatmentRepository.findAllHealthFunds(sDate)) {
                        final ProviderReportVO reportVo = new ProviderReportVO();
                        reportVo.setServiceDate(sDate.format(dtf1));
                        reportVo.setPractitioner(practitioner.getFullName());
                        reportVo.setHealthFund(healthFund.getName());
                        List<Customer> customers = treatmentRepository.findAllCustomers(sDate, practitioner, healthFund, ServiceTypeEnum.ACUPUNCTURE);
                        reportVo.setNumberOfAcupunctureClient(customers.size());
                        customers = treatmentRepository.findAllCustomers(sDate, practitioner, healthFund, ServiceTypeEnum.MASSAGE);
                        reportVo.setNumberOfMassageClient(customers.size());
                        BigDecimal expenseAmtSum = treatmentRepository.getExpenseAmtSum(sDate, practitioner, healthFund, ServiceTypeEnum.ACUPUNCTURE);
                        if (expenseAmtSum == null) {
                            expenseAmtSum = BigDecimal.ZERO;
                        }
                        reportVo.setChargedAcupunctureAmt(CommonUtils.formatCurrencyData(expenseAmtSum));
                        BigDecimal claimedAmtSum = treatmentRepository.getClaimedAmtSum(sDate, practitioner, healthFund, ServiceTypeEnum.ACUPUNCTURE);
                        if (claimedAmtSum == null) {
                            claimedAmtSum = BigDecimal.ZERO;
                        }
                        reportVo.setClaimedAcupunctureAmt(CommonUtils.formatCurrencyData(claimedAmtSum));
                        expenseAmtSum = treatmentRepository.getExpenseAmtSum(sDate, practitioner, healthFund, ServiceTypeEnum.MASSAGE);
                        if (expenseAmtSum == null) {
                            expenseAmtSum = BigDecimal.ZERO;
                        }
                        reportVo.setChargedMassageAmt(CommonUtils.formatCurrencyData(expenseAmtSum));
                        claimedAmtSum = treatmentRepository.getClaimedAmtSum(sDate, practitioner, healthFund, ServiceTypeEnum.MASSAGE);
                        if (claimedAmtSum == null) {
                            claimedAmtSum = BigDecimal.ZERO;
                        }
                        reportVo.setClaimedMassageAmt(CommonUtils.formatCurrencyData(claimedAmtSum));
                        reportVos.add(reportVo);
                    }
                }
            }
        }
        return reportVos;
    }
}
