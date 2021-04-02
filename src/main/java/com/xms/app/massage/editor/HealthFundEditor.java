package com.xms.app.massage.editor;

import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.service.HealthFundService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

@Component
public class HealthFundEditor  extends PropertyEditorSupport {

    @Autowired
    private HealthFundService healthFundService;

    @Override
    public String getAsText() {
        if (getValue() == null) {
            return "";
        }
        HealthFund healthFund = (HealthFund) getValue();
        return healthFund.getName();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            setValue(healthFundService.findByName(text).orElse(null));
        }
    }
}
