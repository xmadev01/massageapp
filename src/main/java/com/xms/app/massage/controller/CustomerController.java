package com.xms.app.massage.controller;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.CustomerService;
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
import java.util.List;

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
    public String createCustomer(@Valid Customer customer, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addCustomer";
        }
        customerService.saveCustomer(customer);
        MessageUtils.addSuccessMessage(redirectAttributes, "The customer has been added successfully.");
        return "redirect:/addCustomer";
    }

    @PostMapping("/loadCustomer/{customerId}")
    public String loadCustomer(@PathVariable long customerId, Model model) {
        Customer customer = customerService.loadCustomer(customerId);
        model.addAttribute("customer", customer);
        return "updateCustomer";
    }

    @PostMapping("/updateCustomer/{customerId}")
    public String updateCustomer(@PathVariable long customerId, @Valid Customer customer, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) throws InvocationTargetException, IllegalAccessException {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
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

    @GetMapping("/getCustomers")
    @ResponseBody
    public List<Customer> getAllCustomers(@RequestParam String term) {
        return customerService.getCustomers(term);
    }
}
