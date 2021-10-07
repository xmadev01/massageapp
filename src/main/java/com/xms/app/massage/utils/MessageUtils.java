package com.xms.app.massage.utils;

import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

public class MessageUtils {

    public static void addSuccessMessage(final RedirectAttributes attributes, final String message) {
        attributes.addFlashAttribute("successMsg", message);
    }

    public static void addErrorMessages(final Model model, final List<FieldError> errors) {
        final List<String> errorMessages = errors.stream()
                                                 .map(fe -> fe.getDefaultMessage())
                                                 .collect(Collectors.toList());
        model.addAttribute("errorMsgs", errorMessages);
    }

    public static void addObjectErrorMessages(final Model model, final List<ObjectError> errors) {
        final List<String> errorMessages = errors.stream()
                .map(fe -> fe.getDefaultMessage())
                .collect(Collectors.toList());
        model.addAttribute("errorMsgs", errorMessages);
    }
}
