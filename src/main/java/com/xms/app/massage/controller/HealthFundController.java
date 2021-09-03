package com.xms.app.massage.controller;

import com.xms.app.massage.editor.HealthFundEditor;
import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.HealthFundService;
import com.xms.app.massage.utils.MessageUtils;
import com.xms.app.massage.validators.HealthFundValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@SessionAttributes("activeItem")
public class HealthFundController {

    @Autowired
    private HealthFundService healthFundService;
    @Autowired
    private MessageSource messageSource;

    @InitBinder("healthFund")
    public void bindValidator(final WebDataBinder binder) {
        binder.setValidator(new HealthFundValidator(messageSource, healthFundService));
        binder.registerCustomEditor(HealthFund.class, new HealthFundEditor());
    }

    @GetMapping("/listHealthFunds")
    public String list(Model model) {
        model.addAttribute("activeItem", "settings");
        return "healthfund";
    }

    @PostMapping("/filterHealthFund")
    @ResponseBody
    public Page<HealthFund> filterHealthFund(@RequestBody PagingRequest pagingRequest) {
        return healthFundService.getPage(pagingRequest);
    }

    @GetMapping("/addHealthFund")
    public String addHealthFund(Model model) {
        model.addAttribute("healthFund", new HealthFund());
        return "addHealthFund";
    }

    @PostMapping("/createHealthFund")
    public String createHealthFund(@ModelAttribute("healthFund") @Valid HealthFund healthFund, BindingResult result,
                                   RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addHealthFund";
        }
        healthFundService.saveHealthFund(healthFund);
        MessageUtils.addSuccessMessage(redirectAttributes, "The health fund has been added successfully.");
        return "redirect:/listHealthFunds";
    }

    @PostMapping("/loadHealthFund/{healthFundId}")
    public String loadHealthFund(@PathVariable long healthFundId, Model model) {
        HealthFund healthFund = healthFundService.loadHealthFund(healthFundId);
        model.addAttribute("healthFund", healthFund);
        return "updateHealthFund";
    }

    @PostMapping("/updateHealthFund/{healthFundId}")
    public String updateHealthFund(@PathVariable long healthFundId, @ModelAttribute HealthFund healthFund, BindingResult result,
                              RedirectAttributes redirectAttributes, Model model) throws InvocationTargetException, IllegalAccessException {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "updateHealthFund";
        }

        HealthFund loadedHealthFund = healthFundService.loadHealthFund(healthFundId);
        BeanUtils.copyProperties(healthFund, loadedHealthFund, "name", "description");
        healthFundService.saveHealthFund(loadedHealthFund);
        MessageUtils.addSuccessMessage(redirectAttributes, "The health fund has been updated successfully.");
        return "redirect:/listHealthFunds";
    }

    @PostMapping("/deleteHealthFund/{healthFundId}")
    public String deleteHealthFund(@PathVariable long healthFundId, Model model) {
        healthFundService.deleteHealthFund(healthFundId);
        return "redirect:/listHealthFunds";
    }

    @GetMapping("/getHealthFunds")
    @ResponseBody
    public List<String> getAllHealthFunds(@RequestParam String term) {
        return healthFundService.getHealthFunds(term);
    }

}
