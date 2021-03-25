package com.xms.app.massage.converter;

import com.xms.app.massage.enums.HealthFundEnum;
import org.springframework.core.convert.converter.Converter;

public class StringToHealthFundEnumConverter implements Converter<String, HealthFundEnum> {

    @Override
    public HealthFundEnum convert(String ordinal) {
        for (HealthFundEnum healthFundEnum : HealthFundEnum.values()) {
            if (healthFundEnum.ordinal() == Integer.parseInt(ordinal)) {
                return healthFundEnum;
            }
        }
        return null;
    }
}
