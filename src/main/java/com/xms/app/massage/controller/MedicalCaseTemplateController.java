package com.xms.app.massage.controller;

import com.xms.app.massage.model.Template;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.TemplateService;
import com.xms.app.massage.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("activeItem")
public class MedicalCaseTemplateController {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private MessageSource messageSource;

    @InitBinder("template")
    public void bindValidator(final WebDataBinder binder) {
    }

    @PostMapping("/getAllTemplates")
    @ResponseBody
    public Page<Template> getAllTemplates(@RequestBody PagingRequest pagingRequest) {
        return templateService.getPage(pagingRequest);
    }

    @GetMapping("/getTemplateById/{templateId}")
    @ResponseBody
    public Template getTemplateById(@PathVariable long templateId) {
        return templateService.loadTemplate(templateId);
    }


    @GetMapping("/listTemplates")
    public String listTemplates(Model model) {
        model.addAttribute("activeItem", "settings");
        return "templates";
    }

    @PostMapping("/addTemplate")
    public String addTemplate(Model model) {
        model.addAttribute("activeItem", "settings");
        model.addAttribute("template", new Template());
        return "addTemplate";
    }

    @GetMapping("/loadTemplate/{templateId}")
    public String loadTemplate(@PathVariable long templateId, Model model) {
        Template template = templateService.loadTemplate(templateId);
        model.addAttribute("template", template);
        return "updateTemplate";
    }

    @PostMapping("/saveTemplate")
    public String saveTemplate(@ModelAttribute Template template, BindingResult result,
                                   RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addTemplate";
        }
        templateService.saveTemplate(template);
        MessageUtils.addSuccessMessage(redirectAttributes, "The template has been added successfully.");
        return "redirect:/listTemplates";
    }

    @PostMapping("/deleteTemplate")
    public String deleteTemplate(@RequestParam long templateId, Model model) {
        templateService.deactivateTemplate(templateId);
        return "redirect:/listTemplates";
    }

}
