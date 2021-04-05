package com.xms.app.massage.controller;

import com.xms.app.massage.editor.LocalDateEditor;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.model.Practitioner;
import com.xms.app.massage.service.ItemService;
import com.xms.app.massage.service.PractitionerService;
import com.xms.app.massage.service.TreatmentService;
import com.xms.app.massage.utils.MessageUtils;
import com.xms.app.massage.validators.TreatmentValidator;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
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

    @InitBinder
    public void bindValidator(final WebDataBinder binder) {
        binder.setValidator(new TreatmentValidator(messageSource, treatmentService));
        binder.registerCustomEditor(LocalDate.class, localDateEditor);
    }

    @GetMapping("/addTreatment")
    public String addService(Model model) {
        populateModel(model);
        return "addTreatment";
    }

    @PostMapping("/assignPractitioner")
    public String assignService(@Valid TreatmentVO treatmentVO, BindingResult result,
                                RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            populateModel(model);
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addTreatment";
        }
        treatmentService.assignCustomerToPractitioner(treatmentVO);
        return "home";
    }

    private void populateModel(Model model) {
        final List<Practitioner> practitioners = practitionerService.getAllPractitioners();
        final List<Item> items = itemService.getAllItems().stream()
                .filter(Item::isActive)
                .collect(Collectors.toList());

        model.addAttribute("practitioners", practitioners);
        model.addAttribute("items", items);
        model.addAttribute("serviceDate", LocalDate.now().format(dtf));
    }

}
