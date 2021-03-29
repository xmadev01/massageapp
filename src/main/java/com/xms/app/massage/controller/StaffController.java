package com.xms.app.massage.controller;

import com.xms.app.massage.model.Staff;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.StaffService;
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
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/listStaff")
    public String list() {
        return "staff";
    }

    @PostMapping("/filterStaff")
    @ResponseBody
    public Page<Staff> filterStaff(@RequestBody PagingRequest pagingRequest) {
        return staffService.getPage(pagingRequest);
    }

    @GetMapping("/addStaff")
    public String addStaff(Model model) {
        model.addAttribute("staff", new Staff());
        return "addStaff";
    }

    @PostMapping("/createStaff")
    public String createStaff(@Valid Staff staff, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addStaff";
        }
        staffService.saveStaff(staff);
        MessageUtils.addSuccessMessage(redirectAttributes, "The staff has been added successfully.");
        return "redirect:/addStaff";
    }

    @PostMapping("/updateStaff/{staffId}")
    public String updateStaff(@PathVariable long staffId, @Valid Staff staff, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) throws InvocationTargetException, IllegalAccessException {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "updateStaff";
        }

        Staff loadedStaff = staffService.loadStaff(staffId);
        BeanUtils.copyProperties(loadedStaff, staff);
        staffService.saveStaff(loadedStaff);
        return "redirect:/listStaff";
    }

    @PostMapping("/loadStaff/{staffId}")
    public String loadStaff(@PathVariable long staffId, Model model) {
        Staff staff = staffService.loadStaff(staffId);
        model.addAttribute("staff", staff);
        return "updateStaff";
    }

    @PostMapping("/deactivateStaff/{staffId}")
    public String deactivateStaff(@PathVariable long staffId, Model model) {
        staffService.deactivateStaff(staffId);
        return "redirect:/listStaff";
    }


    @PostMapping("/deleteStaff/{staffId}")
    public String deleteStaff(@PathVariable long staffId, Model model) {
        staffService.deleteStaff(staffId);
        return "redirect:/listStaff";
    }
}
