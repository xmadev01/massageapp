package com.xms.app.massage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionController {

    private final Logger log = LoggerFactory.getLogger(ApplicationExceptionController.class);

    @ExceptionHandler
    public String exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return "redirect:/error";
    }
}
