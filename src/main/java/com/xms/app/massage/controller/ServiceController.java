package com.xms.app.massage.controller;

import com.xms.app.massage.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ServiceController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/addService")
    public String addService() {
        return "addService";
    }

}
