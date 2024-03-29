package com.xms.app.massage.validators;

import com.xms.app.massage.service.TreatmentService;
import com.xms.app.massage.vo.TreatmentVO;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;

public class TreatmentValidator implements Validator {

    private MessageSource messageSource;
    private TreatmentService treatmentService;

    public TreatmentValidator(final MessageSource messageSource, final TreatmentService treatmentService) {
            this.messageSource =  messageSource;
            this.treatmentService = treatmentService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TreatmentVO.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final TreatmentVO treatmentVo = (TreatmentVO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerName", null,
                messageSource.getMessage("treatment.customerName.not.empty", null, Locale.getDefault()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "practitionerId", null,
                messageSource.getMessage("treatment.practitionerId.not.selected", null, Locale.getDefault()));
        if (treatmentVo.getItemIds() == null) {
            errors.rejectValue("itemIds", null, messageSource.getMessage("treatment.item.not.selected", null, Locale.getDefault()));
        }
    }
}
