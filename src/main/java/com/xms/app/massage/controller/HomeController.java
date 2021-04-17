package com.xms.app.massage.controller;

import com.xms.app.massage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;

@Controller
@SessionAttributes("activeItem")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("activeItem", "home");
        model.addAttribute("viewMode", "day");
        model.addAttribute("currentDay", LocalDate.now());
        return "home";
    }
}
