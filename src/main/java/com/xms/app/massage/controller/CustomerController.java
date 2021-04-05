package com.xms.app.massage.controller;

import com.xms.app.massage.editor.HealthFundEditor;
import com.xms.app.massage.editor.LocalDateEditor;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.CustomerService;
import com.xms.app.massage.utils.MessageUtils;
import com.xms.app.massage.validators.CustomerValidator;
import org.apache.commons.beanutils.BeanUtils;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private HealthFundEditor healthFundEditor;
    @Autowired
    private LocalDateEditor localDateEditor;

    @InitBinder
    public void bindValidator(final WebDataBinder binder) {
        binder.setValidator(new CustomerValidator(messageSource, customerService));
        binder.registerCustomEditor(HealthFund.class, healthFundEditor);
        binder.registerCustomEditor(LocalDate.class, localDateEditor);
    }

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
        populateDayMonthYear(model);
        return "addCustomer";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@Valid Customer customer, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            populateDayMonthYear(model);
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            return "addCustomer";
        }
        customerService.saveCustomer(customer);
        MessageUtils.addSuccessMessage(redirectAttributes, "The customer has been added successfully.");
        return "redirect:/listCustomers";
    }

    @PostMapping("/loadCustomer/{customerId}")
    public String loadCustomer(@PathVariable long customerId, Model model) {
        Customer customer = customerService.loadCustomer(customerId);
        model.addAttribute("customer", customer);
        populateDayMonthYear(model);
        return "updateCustomer";
    }

    @PostMapping("/updateCustomer/{customerId}")
    public String updateCustomer(@PathVariable long customerId, @Valid Customer customer, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) throws InvocationTargetException, IllegalAccessException {
        if (result.hasErrors()) {
            MessageUtils.addErrorMessages(model, result.getFieldErrors());
            populateDayMonthYear(model);
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
    public List<String> getAllCustomers(@RequestParam String term) {
        return customerService.getCustomers(term).stream()
                              .map(Customer::getFullName)
                              .collect(Collectors.toList());
    }

    private void populateDayMonthYear(Model model) {
        final List<Integer> days = IntStream.range(1, 32).boxed().collect(Collectors.toList());
        final List<Integer> months = IntStream.range(1, 13).boxed().collect(Collectors.toList());
        final List<Integer> years = IntStream.range(1900, 2022).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList());
        model.addAttribute("days", days);
        model.addAttribute("months", months);
        model.addAttribute("years", years);
    }
}
