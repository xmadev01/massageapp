package com.xms.app.massage.controller;

import com.xms.app.massage.service.CustomerService;
import com.xms.app.massage.service.PractitionerService;
import com.xms.app.massage.vo.ServiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TreatmentController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PractitionerService practitionerService;

    @GetMapping("/addTreatment")
    public String addService(Model model) {
        final List<String> allStaff = practitionerService.getAllPractitioners();
        model.addAttribute("allPractitioner", allStaff);
        return "addTreatment";
    }

    @PostMapping("/assignTreatment")
    public String assignService(ServiceVO serviceVO) {
        customerService.assignCustomerToServices(serviceVO);
        return "home";
    }

}
