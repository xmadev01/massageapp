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
import net.sf.jasperreports.phantomjs.InetUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
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
        final long customerId = Long.parseLong(treatmentVO.getCustomerName().split("-")[2].trim());
        Customer customer = customerService.loadCustomer(customerId);

        if (customer != null) {
            if (treatmentVO.getItemIds() != null) {
                treatmentVO.getItemIds().forEach(itemId -> {
                    final Treatment treatment = new Treatment();
                    final Item item = itemService.findById(itemId).get();
                    treatment.setCustomer(customer);
                    treatment.setServiceDate(treatmentVO.getServiceDate());
                    treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
                    treatment.setItem(item);
                    treatment.setMedicalCaseRecord(treatmentVO.getMedicalCaseRecord());
                    treatment.setExpenseAmt(item.getPrice());
                    treatment.setClaimedAmt(item.getPrice().multiply(BigDecimal.valueOf(customer.getRebateRate().doubleValue()))
                            .divide(BigDecimal.valueOf(100)));
                    treatment.setCreatedDate(LocalDateTime.now());
                    treatmentRepository.save(treatment);
                });
            }
            if (StringUtils.isNotBlank(treatmentVO.getOtherItemName())) {
                final Treatment treatment = new Treatment();
                final Item item = new Item();
                item.setName(treatmentVO.getOtherItemName());
                item.setPrice(BigDecimal.ZERO);
                item.setType(ServiceTypeEnum.OTHER);
                treatment.setCustomer(customer);
                treatment.setServiceDate(treatmentVO.getServiceDate());
                treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
                treatment.setItem(item);
                treatment.setMedicalCaseRecord(treatmentVO.getMedicalCaseRecord());
                treatment.setExpenseAmt(treatmentVO.getExpenseAmt() == null ? BigDecimal.ZERO : treatmentVO.getExpenseAmt());
                treatment.setClaimedAmt(treatmentVO.getClaimedAmt() == null ? BigDecimal.ZERO : treatmentVO.getClaimedAmt());
                treatment.setCreatedDate(LocalDateTime.now());
                treatmentRepository.save(treatment);
            }
        }
    }

    @Override
    public void assignCustomerToPractitionerForUpdate(final SingleTreatmentVO treatmentVO) {
        final long customerId = Long.parseLong(treatmentVO.getCustomerName().split("-")[2].trim());
        Customer customer = customerService.loadCustomer(customerId);

        if (customer != null) {
            final Optional<Treatment> treatmentOpt = treatmentRepository.findById(treatmentVO.getTreatmentId());
            if (treatmentOpt.isPresent()) {
                Treatment treatment = treatmentOpt.get();
                treatment.setCustomer(customer);
                treatment.setServiceDate(treatmentVO.getServiceDate());
                treatment.setPractitioner(practitionerService.loadPractitioner(Long.parseLong(treatmentVO.getPractitionerId())));
                treatment.setMedicalCaseRecord(treatmentVO.getMedicalCaseRecord());
                treatment.setCreatedDate(LocalDateTime.now());
                treatment.setDuration(treatmentVO.getDuration());
                treatment.setExpenseAmt(treatmentVO.getExpenseAmt());
                treatment.setClaimedAmt(treatmentVO.getClaimedAmt());
                treatmentRepository.save(treatment);
            }
        }
    }

    public List<ConsultationDto> findAllOrderByCustomer(PagingRequest pagingRequest) {
        StringBuilder querySQLBuilder = new StringBuilder()
                .append("select t.id as treatmentId, t.service_date, t.customer, i.id as itemId, t.duration, c.health_fund, t.expense_amt, t.claimed_amt, t.medical_case_record from treatment t ")
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
            treatmentVO.setCustomerName(treatment.get().getCustomer().getFullNameBirthDayId());
            treatmentVO.setPractitionerId(String.valueOf(treatment.get().getPractitioner().getId()));
            treatmentVO.setMedicalCaseRecord(treatment.get().getMedicalCaseRecord());
            treatmentVO.setItemName(treatment.get().getItem().getDisplayName());
            treatmentVO.setDuration(treatment.get().getDuration());
            treatmentVO.setExpenseAmt(treatment.get().getExpenseAmt());
            treatmentVO.setClaimedAmt(treatment.get().getClaimedAmt());
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
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new ClassPathResource("report/Invoice-z.jasper", getClass().getClassLoader()).getFile());

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

        if (treatment.getItem().getType() == ServiceTypeEnum.ACUPUNCTURE && StringUtils.isNotBlank(treatment.getCustomer().getHealthFund().getProviderNumA())) {
            parameters.put("healthFundName", treatment.getCustomer().getHealthFund().getDescription() + ":");
            parameters.put("insuranceProviderNum", treatment.getCustomer().getHealthFund().getProviderNumA());
        } else if (treatment.getItem().getType() == ServiceTypeEnum.MASSAGE && StringUtils.isNotBlank(treatment.getCustomer().getHealthFund().getProviderNumM())) {
            parameters.put("healthFundName", treatment.getCustomer().getHealthFund().getDescription() + ":");
            parameters.put("insuranceProviderNum", treatment.getCustomer().getHealthFund().getProviderNumM());
        }
        List<TreatmentInvoiceVO> invoices = new ArrayList<>();
        treatments.stream().forEach(t -> {
            TreatmentInvoiceVO tiVo = new TreatmentInvoiceVO();
            tiVo.setServiceDate(t.getServiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            tiVo.setItemName(t.getItemDisplayName());
            tiVo.setPrice("$" + NumberFormat.getCurrencyInstance().format(t.getExpenseAmt()));
            invoices.add(tiVo);
        });

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(invoices));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    @Override
    public boolean validateTreatments(List<Long> treatmentIds, List<ObjectError> errors) {
        final long numOfCustomer = treatmentIds.stream().map(treatmentId -> treatmentRepository.findById(treatmentId).get().getCustomer().getId()).distinct().count();
        if (numOfCustomer > 1) {
            errors.add(new ObjectError("", "Only the treatment for the same customer can be updated at a time."));
        }
        return errors.isEmpty();
    }

}
