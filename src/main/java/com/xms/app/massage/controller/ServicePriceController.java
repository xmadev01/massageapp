package com.xms.app.massage.controller;

import com.xms.app.massage.model.ServicePrice;
import com.xms.app.massage.service.ServicePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ServicePriceController {

    @Autowired
    private ServicePriceService servicePriceService;

    @GetMapping("/getAllServicePrices")
    public String getAllServicePrices(Model model) {
        final List<ServicePrice> servicePrices = servicePriceService.getAllServicePrices();
        model.addAttribute("servicePrices", servicePrices);
        return "servicePrices";
    }

}
