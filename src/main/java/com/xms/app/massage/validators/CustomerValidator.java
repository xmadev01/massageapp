package com.xms.app.massage.validators;

import com.xms.app.massage.model.Customer;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.CustomerService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;

public class CustomerValidator implements Validator {

    private MessageSource messageSource;
    private CustomerService customerService;

    public CustomerValidator(final MessageSource messageSource, final CustomerService customerService) {
            this.messageSource =  messageSource;
            this.customerService = customerService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.equals(aClass) || PagingRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (target instanceof Customer) {
            final Customer customer = (Customer) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", null,
                    messageSource.getMessage("customer.firstName.not.empty", null, Locale.getDefault()));

            if (!EmailValidator.getInstance().isValid(customer.getEmail())) {
                errors.rejectValue("email", null, messageSource.getMessage("customer.email.invalid", null, Locale.getDefault()));
            }
            if (customer.getHealthFund() != null && customer.getRebateRate() == null) {
                errors.rejectValue("rebateRate", null, messageSource.getMessage("customer.rebateRate.not.empty", null, Locale.getDefault()));
            }
            if (customer.getRebateRate() != null  && (customer.getRebateRate() < 1 || customer.getRebateRate() > 100)) {
                errors.rejectValue("rebateRate", null, messageSource.getMessage("customer.rebateRate.invalid", null, Locale.getDefault()));
            }
        }
    }
}
