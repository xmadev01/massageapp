package com.xms.app.massage.service;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.enums.ServiceTypeEnum;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.model.Treatment;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.TreatmentRepository;
import com.xms.app.massage.transformer.ConsultationTransformer;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.SingleTreatmentVO;
import com.xms.app.massage.vo.TreatmentInvoiceVO;
import com.xms.app.massage.vo.TreatmentVO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TreatmentServiceImpl extends AbstractXMSService implements TreatmentService {

    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PractitionerService practitionerService;
    @Autowired
    private ConsultationTransformer consultationTransformer;

    @Override
    public Optional<Treatment> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void saveTreatment(Treatment treatment) {
        treatmentRepository.save(treatment);
    }

    @Override
    public Page<ConsultationVO> getPage(PagingRequest pagingRequest) {

        List<ConsultationDto> consultationDtos = findAllOrderByCustomer(pagingRequest);
        List<ConsultationVO> consultations = new ArrayList<>();

        if (!consultationDtos.isEmpty()) {
            consultations = populateConsultationVO(consultationDtos);
        }

        final List<ConsultationVO> filteredConsultations = consultations.stream()
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());
        long count = consultations.stream()
                .count();
        Page<ConsultationVO> page = new Page<>(filteredConsultations);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    @Override
    public void assignCustomerToPractitioner(final TreatmentVO treatmentVO) {
        final String[] names = treatmentVO.getCustomerName().split(" ");
        Optional<Customer> customerOpt;
        if (names.length == 3) {
            customerOpt = customerService.findByFirstNameLastName(names[0], names[1], names[2]);
        } else if (names.length == 2) {
            customerOpt = customerService.findByFirstNameLastName(names[0], "", names[1]);
        } else {
            customerOpt = customerService.findByFirstName(names[0]);
        }
        if (customerOpt.isPresent()) {
            treatmentVO.getItemIds().forEach(itemId -> {
                final Treatment treatment = new Treatment();
                final Item item = itemService.findById(itemId).get();
                treatment.setCustomer(customerOpt.get());
                treatment.setServiceDate(treatmentVO.getServiceDate());
                treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
                treatment.setItem(item);
                treatment.setMedicalCaseRecord(treatmentVO.getMedicalCaseRecord());
                treatment.setExpenseAmt(item.getPrice());
                treatment.setClaimedAmt(item.getPrice().multiply(BigDecimal.valueOf(customerOpt.get().getRebateRate().doubleValue()))
                        .divide(BigDecimal.valueOf(100)));
                treatment.setCreatedDate(LocalDateTime.now());
                treatmentRepository.save(treatment);
            });
        }
    }

    @Override
    public void assignCustomerToPractitionerForUpdate(final SingleTreatmentVO treatmentVO) {
        final String[] names = treatmentVO.getCustomerName().split(" ");
        Optional<Customer> customerOpt;
        if (names.length == 3) {
            customerOpt = customerService.findByFirstNameLastName(names[0], names[1], names[2]);
        } else {
            customerOpt = customerService.findByFirstNameLastName(names[0], "", names[1]);
        }
        if (customerOpt.isPresent()) {
            final Optional<Treatment> treatmentOpt = treatmentRepository.findById(treatmentVO.getTreatmentId());
            if (treatmentOpt.isPresent()) {
                Treatment treatment = treatmentOpt.get();
                treatment.setCustomer(customerOpt.get());
                treatment.setServiceDate(treatmentVO.getServiceDate());
                treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
                treatment.setMedicalCaseRecord(treatmentVO.getMedicalCaseRecord());
                treatment.setCreatedDate(LocalDateTime.now());
                treatmentRepository.save(treatment);
            }
        }
    }

    public List<ConsultationDto> findAllOrderByCustomer(PagingRequest pagingRequest) {
        StringBuilder querySQLBuilder = new StringBuilder()
                .append("select t.id as templateId, t.service_date, t.customer, i.id as itemId, c.health_fund, t.expense_amt, t.claimed_amt, t.medical_case_record from treatment t ")
                .append("inner join item i on t.item = i.id ")
                .append("inner join customer c on t.customer = c.id ");
        if (StringUtils.isNotBlank(pagingRequest.getFromDate()) && StringUtils.isNotBlank(pagingRequest.getToDate())) {
            LocalDate startDate = LocalDate.parse(pagingRequest.getFromDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(pagingRequest.getToDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1);
            querySQLBuilder.append("where t.service_date >= '").append(startDate.format(dtf)).append("' ");
            querySQLBuilder.append("and t.service_date < '").append(endDate.format(dtf)).append("' ");
        } else if (StringUtils.isNotBlank(pagingRequest.getFromDate())) {
            LocalDate startDate = LocalDate.parse(pagingRequest.getFromDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            querySQLBuilder.append("where t.service_date >= '").append(startDate.format(dtf)).append("' ");
        } else if (StringUtils.isNotBlank(pagingRequest.getToDate())) {
            LocalDate endDate = LocalDate.parse(pagingRequest.getToDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1);
            querySQLBuilder.append("where t.service_date < '").append(endDate.format(dtf)).append("' ");
        }
        if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
            if (StringUtils.isBlank(pagingRequest.getFromDate()) && StringUtils.isBlank(pagingRequest.getToDate())) {
                querySQLBuilder.append("where LOWER(CONCAT(c.first_name, c.middle_name, c.last_name)) like '%" + pagingRequest.getSearch().getValue().toLowerCase() + "%' ");
            } else {
                querySQLBuilder.append("and LOWER(CONCAT(c.first_name, c.middle_name, c.last_name)) like '%" + pagingRequest.getSearch().getValue().toLowerCase() + "%' ");
            }
        }
        querySQLBuilder.append("and t.active = 1 ");
        querySQLBuilder.append("order by c.first_name asc, c.middle_name asc, c.last_name asc, i.type asc, t.service_date asc, i.name asc");

        return em.unwrap(Session.class)
                .createNativeQuery(querySQLBuilder.toString())
                .setResultTransformer(consultationTransformer).list();

    }

    @Override
    public SingleTreatmentVO loadSingleTreatment(final long treatmentId) {
        Optional<Treatment> treatment = treatmentRepository.findById(treatmentId);
        SingleTreatmentVO treatmentVO = new SingleTreatmentVO();
        if (treatment.isPresent()) {
            treatmentVO.setTreatmentId(treatmentId);
            treatmentVO.setServiceDate(treatment.get().getServiceDate());
            treatmentVO.setCustomerName(treatment.get().getCustomer().getFullName());
            treatmentVO.setPractitionerId(String.valueOf(treatment.get().getPractitioner().getId()));
            treatmentVO.setMedicalCaseRecord(treatment.get().getMedicalCaseRecord());
            treatmentVO.setItemName(treatment.get().getItem().getDisplayName());
        }
        return treatmentVO;
    }

    @Override
    public void deactivateTreatment(final long treatmentId) {
        Optional<Treatment> treatment = treatmentRepository.findById(treatmentId);
        if (treatment.isPresent()) {
            treatment.get().setActive(false);
        }
    }

    @Override
    public void downloadInvoice(List<Long> treatmentIds, HttpServletResponse response) throws JRException, IOException {
        final String fileName = "Invoice.pdf";
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("classpath:report/Invoice.jasper"));

        final Treatment treatment = treatmentRepository.findById(new Long(treatmentIds.get(0))).get();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("patientName", treatment.getCustomer().getFullName());
        parameters.put("providerName", treatment.getPractitioner().getFullName());
        List<Treatment> treatments = treatmentRepository.findAllByTreatmentIds(treatmentIds);
        final double totalAmt = treatments.stream().mapToDouble(t -> t.getExpenseAmt().doubleValue())
                .reduce(0, (a, b) -> a + b);
        parameters.put("totalAmt", "$" + NumberFormat.getCurrencyInstance().format(totalAmt));
        parameters.put("associationNum", treatment.getPractitioner().getAssociationNum());
        parameters.put("ARHGNum", treatment.getPractitioner().getArhgNum());
        parameters.put("healthFundName", treatment.getCustomer().getHealthFund().getDescription() + ":");
        if (treatment.getItem().getType() == ServiceTypeEnum.ACUPUNCTURE) {
            parameters.put("insuranceProviderNum", treatment.getCustomer().getHealthFund().getProviderNumA());
        } else if (treatment.getItem().getType() == ServiceTypeEnum.MASSAGE) {
            parameters.put("insuranceProviderNum", treatment.getCustomer().getHealthFund().getProviderNumM());
        }
        parameters.put("insuranceProviderNum", treatment.getPractitioner().getArhgNum());
        List<TreatmentInvoiceVO> invoices = new ArrayList<>();
        treatments.stream().forEach(t -> {
            TreatmentInvoiceVO tiVo = new TreatmentInvoiceVO();
            tiVo.setServiceDate(t.getServiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            tiVo.setItemName(t.getItem().getDisplayName());
            tiVo.setPrice("$" + NumberFormat.getCurrencyInstance().format(t.getExpenseAmt()));
            invoices.add(tiVo);
        });

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(invoices));
        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=" + fileName);
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

}
