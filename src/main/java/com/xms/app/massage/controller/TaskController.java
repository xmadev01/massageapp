package com.xms.app.massage.controller;

import com.xms.app.massage.service.CustomerService;
import com.xms.app.massage.service.StaffService;
import com.xms.app.massage.vo.ServiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private StaffService staffService;

    @GetMapping("/addTask")
    public String addService(Model model) {
        final List<String> allStaff = staffService.getAllStaff();
        model.addAttribute("allStaff", allStaff);
        return "addTask";
    }

    @PostMapping("/assignTask")
    public String assignService(ServiceVO serviceVO) {
        customerService.assignCustomerToServices(serviceVO);
        return "home";
    }

}
