package com.github.luchici.thymeleaf.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.net.BindException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {

    // @ExceptionHandler(Exception.class)
    public String exceptionHandler(Model model, Exception exception, BindException bindException) {
        log.error("Exception: ", exception);
        log.error("Binding Exception", bindException.getMessage());
        model.addAttribute("exception",exception.getMessage());
        return "/error";
    }


}
