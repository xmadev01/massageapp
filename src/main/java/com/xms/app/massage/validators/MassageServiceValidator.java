package com.xms.app.massage.validators;


import com.xms.app.massage.model.MassageService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MassageServiceValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return MassageService.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
