package com.xms.app.massage.controller;

import com.xms.app.massage.model.Practitioner;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.PractitionerService;
import com.xms.app.massage.utils.MessageUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller
@SessionAttributes("activeItem")
public class PractitionerController {

    @Autowired
    private PractitionerService practitionerService;

    @GetMapping("/listPractitioner")
    public String list(Model model) {
        model.addAttribute("activeItem", "settings");
        return "practitioner";
    }

    @PostMapping("/filterPractitioner")
    @ResponseBody
    public Page<Practitioner> filterStaff(@RequestBody PagingRequest pagingRequest) {
        return practitionerService.getPage(pagingRequest);
    }

    @GetMapping("/addPractitioner")
    public String addPractitioner(Model model) {
        model.addAttribute("practitioner", new Practitioner());
        return "addPractitioner";
    }

    @PostMapping("/createPractitioner")
    public String createPractitioner(@Valid Practitioner practitioner, BindingResult result,
                              RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addPractitioner";
        }
        practitionerService.savePractitioner(practitioner);
        MessageUtils.addSuccessMessage(redirectAttributes, "The practitioner has been added successfully.");
        return "redirect:/listPractitioner";
    }

    @PostMapping("/updatePractitioner/{staffId}")
    public String updatePractitioner(@PathVariable long staffId, @Valid Practitioner practitioner, BindingResult result,
                              RedirectAttributes redirectAttributes, Model model) throws InvocationTargetException, IllegalAccessException {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "updatePractitioner";
        }

        Practitioner loadedPractitioner = practitionerService.loadPractitioner(staffId);
        BeanUtils.copyProperties(loadedPractitioner, practitioner);
        practitionerService.savePractitioner(loadedPractitioner);
        MessageUtils.addSuccessMessage(redirectAttributes, "The practitioner has been updated successfully.");
        return "redirect:/listPractitioner";
    }

    @PostMapping("/loadPractitioner/{staffId}")
    public String loadStaff(@PathVariable long staffId, Model model) {
        Practitioner practitioner = practitionerService.loadPractitioner(staffId);
        model.addAttribute("practitioner", practitioner);
        return "updatePractitioner";
    }

    @PostMapping("/deletePractitioner/{staffId}")
    public String deactivatePractitioner(@PathVariable long staffId, RedirectAttributes redirectAttributes, Model model) {
        practitionerService.deactivatePractitioner(staffId);
        MessageUtils.addSuccessMessage(redirectAttributes, "The practitioner has been deleted successfully.");
        return "redirect:/listPractitioner";
    }
}
