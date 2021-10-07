package com.xms.app.massage.controller;

import com.xms.app.massage.editor.LocalDateEditor;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.*;
import com.xms.app.massage.utils.MessageUtils;
import com.xms.app.massage.validators.TreatmentValidator;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.SingleTreatmentVO;
import com.xms.app.massage.vo.TreatmentVO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"activeItem", "practitioners", "items", "templates"})
public class TreatmentController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private TreatmentService treatmentService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private PractitionerService practitionerService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private LocalDateEditor localDateEditor;
    @Autowired
    private MessageSource messageSource;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @InitBinder("treatmentVo")
    public void bindValidator(final WebDataBinder binder) {
        binder.setValidator(new TreatmentValidator(messageSource, treatmentService));
        binder.registerCustomEditor(LocalDate.class, localDateEditor);
    }

    @GetMapping("/addTreatment")
    public String addService(Model model) {
        final TreatmentVO treatmentVO = new TreatmentVO();
        treatmentVO.setServiceDate(LocalDate.now());
        model.addAttribute("treatmentVo", treatmentVO);
        model.addAttribute("practitioners", practitionerService.getAllPractitioners());
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("templates", templateService.getAllTemplates());
        return "addTreatment";
    }

    @PostMapping("/assignPractitioner")
    public String assignService(@ModelAttribute("treatmentVo") @Valid TreatmentVO treatmentVO, BindingResult result,
                                RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addTreatment";
        }
        treatmentService.assignCustomerToPractitioner(treatmentVO);
        MessageUtils.addSuccessMessage(redirectAttributes, "The treatment has been assigned successfully.");
        return "redirect:/listTreatments";
    }

    @PostMapping("/assignPractitionerForUpdate")
    public String assignServiceForUpdate(@ModelAttribute("singleTreatmentVo") @Valid SingleTreatmentVO singleTreatmentVo, BindingResult result,
                                RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addTreatment";
        }
        treatmentService.assignCustomerToPractitionerForUpdate(singleTreatmentVo);
        MessageUtils.addSuccessMessage(redirectAttributes, "The treatment has been assigned successfully.");
        return "redirect:/listTreatments";
    }

    @GetMapping("/listTreatments")
    public String list(Model model) {
        model.addAttribute("activeItem", "treatment");
        model.addAttribute("viewMode", "day");
        model.addAttribute("currentDay", LocalDate.now());
        return "treatments";
    }

    @PostMapping("/filterTreatments")
    @ResponseBody
    public Page<ConsultationVO> filterTreatments(@RequestBody PagingRequest pagingRequest) {
        return treatmentService.getPage(pagingRequest);
    }
    @PostMapping("/filterHomeTreatments")
    @ResponseBody
    public Page<ConsultationVO> filterHomeTreatments(@RequestBody PagingRequest pagingRequest) {
        return homeService.getPage(pagingRequest);
    }

    @GetMapping("/loadTreatment/{treatmentId}")
    public String loadTreatment(@PathVariable long treatmentId, Model model) {
        SingleTreatmentVO singleTreatmentVo = treatmentService.loadSingleTreatment(treatmentId);
        model.addAttribute("singleTreatmentVo", singleTreatmentVo);
        model.addAttribute("practitioners", practitionerService.getAllPractitioners());
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("templates", templateService.getAllTemplates());
        return "updateTreatment";
    }

    @PostMapping("/deleteTreatment")
    public String deleteTreatment(@RequestParam List<Integer> treatmentIds, Model model) {
        treatmentIds.forEach(treatmentId -> {
            treatmentService.deactivateTreatment(treatmentId);
        });
        return "redirect:/listTreatments";
    }

    @PostMapping("/downloadInvoice")
    public void downloadInvoice(HttpServletRequest request, HttpServletResponse response, @RequestParam List<Long> treatmentIds, Model model) throws JRException, IOException {

        treatmentService.downloadInvoice(treatmentIds, response);
        final ServletOutputStream outputStream = response.getOutputStream();
    }

}
