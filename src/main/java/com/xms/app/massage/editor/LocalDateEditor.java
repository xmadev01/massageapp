package com.xms.app.massage.editor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateEditor extends PropertyEditorSupport {

    private static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String getAsText() {
        if (getValue() == null) {
            return "";
        }
        LocalDate date = (LocalDate) getValue();
        return date.format(dtf2);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            setValue(LocalDate.parse(text, dtf1));
        }
    }
}
