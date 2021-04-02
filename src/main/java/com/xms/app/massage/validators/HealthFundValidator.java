package com.xms.app.massage.validators;


import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.HealthFundService;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;

public class HealthFundValidator implements Validator {

    private MessageSource messageSource;
    private HealthFundService healthFundService;

    public HealthFundValidator(final MessageSource messageSource, final HealthFundService healthFundService) {
            this.messageSource =  messageSource;
            this.healthFundService = healthFundService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return HealthFund.class.equals(aClass) || PagingRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof HealthFund) {
            final HealthFund healthFund = (HealthFund) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null,
                    messageSource.getMessage("healthFund.name.not.empty", null, Locale.getDefault()));
            if (healthFundService.findByName(healthFund.getName().toLowerCase()).isPresent()) {
                errors.rejectValue("name", null, messageSource.getMessage("healthFund.name.already.exist", null, Locale.getDefault()));
            }
        }
    }
}
