package com.xms.app.massage.controller;

import com.xms.app.massage.editor.LocalDateEditor;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.ItemService;
import com.xms.app.massage.service.PractitionerService;
import com.xms.app.massage.service.TreatmentService;
import com.xms.app.massage.utils.MessageUtils;
import com.xms.app.massage.validators.TreatmentValidator;
import com.xms.app.massage.vo.ConsultationVO;
import com.xms.app.massage.vo.TreatmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@SessionAttributes({"practitioners", "items"})
public class TreatmentController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private TreatmentService treatmentService;
    @Autowired
    private PractitionerService practitionerService;
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

    @GetMapping("/listTreatments")
    public String list() {
        return "treatments";
    }

    @PostMapping("/filterTreatments")
    @ResponseBody
    public Page<ConsultationVO> filterTreatments(@RequestBody PagingRequest pagingRequest) {
        return treatmentService.getPage(pagingRequest);
    }
}
