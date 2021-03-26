package com.xms.app.massage.controller;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.CustomerService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/listCustomers")
    public String list() {
        return "customers";
    }

    @PostMapping("/filterCustomers")
    @ResponseBody
    public Page<Customer> filterCustomers(@RequestBody PagingRequest pagingRequest) {
        return customerService.getPage(pagingRequest);
    }

    @GetMapping("/addCustomer")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "addCustomer";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@Valid Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addCustomer";
        }
        customerService.saveCustomer(customer);
        return "redirect:/home";
    }

    @PostMapping("/loadCustomer/{customerId}")
    public String loadCustomer(@PathVariable long customerId, Model model) {
        Customer customer = customerService.loadCustomer(customerId);
        model.addAttribute("customer", customer);
        return "updateCustomer";
    }

    @PostMapping("/updateCustomer/{customerId}")
    public String updateCustomer(@PathVariable long customerId, @Valid Customer customer, BindingResult result, Model model) throws InvocationTargetException, IllegalAccessException {
        if (result.hasErrors()) {
            return "updateCustomer";
        }

        Customer loadedCustomer = customerService.loadCustomer(customerId);
        BeanUtils.copyProperties(loadedCustomer, customer);
        customerService.saveCustomer(loadedCustomer);
        return "redirect:/listCustomers";
    }

    @PostMapping("/deactivateCustomer/{customerId}")
    public String deactivateCustomer(@PathVariable long customerId, Model model) {
        customerService.deactivateCustomer(customerId);
        return "redirect:/listCustomers";
    }


    @PostMapping("/deleteCustomer/{customerId}")
    public String deleteCustomer(@PathVariable long customerId, Model model) {
        customerService.deleteCustomer(customerId);
        return "redirect:/listCustomers";
    }
}
