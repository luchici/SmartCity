package com.github.luchici.authservice.controllers;

import com.github.luchici.authservice.exceptions.AppException;
import com.github.luchici.authservice.model.dtos.ConstraintViolationDto;
import com.github.luchici.authservice.model.dtos.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {
    @ExceptionHandler({AppException.class, RuntimeException.class, ServletException.class})
    public ResponseEntity<String> handleAppException(final AppException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .internalServerError()
                .body("Something went wrong, please try again");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException(final Exception ex) {
        log.error("Bad Credentials Exception - ", ex);
        final ExceptionResponseDto exceptionDto = new ExceptionResponseDto("Incorrect password or username. Please try again");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exceptionDto);
    }

    // Authorization Exception
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAuthorizationException(final AppException ex) {
        log.error("The user doesn't have enough authorities - " + ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("The user doesn't have enough authorities" + ex.getMessage());
    }

    // Throw when exception to register new user
    @ExceptionHandler(DbActionExecutionException.class)
    public ResponseEntity<ExceptionResponseDto> handleDbActionExecutionException(final Exception ex) {
        log.error(ex.getMessage(), ex);
        final Throwable causeException = ex.getCause();
        final Throwable causeCauseException = causeException.getCause();
        final String returnMessage = causeCauseException.getMessage().substring(
                        causeCauseException.getMessage().indexOf("Key") + 4,
                        causeCauseException.getMessage().length() - 1).
                replaceAll("\\)", "").
                replaceAll("\\(", "");
        final ExceptionResponseDto exceptionDto = new ExceptionResponseDto(returnMessage);
        return ResponseEntity
                .badRequest()
                .body(exceptionDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ConstraintViolationDto> handleConstraintViolationException(final ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        Set<String> violations = ex.getConstraintViolations().stream()
                .map(constraint -> getPropertyName(constraint.getPropertyPath()) + " - " + constraint.getMessage())
                .collect(Collectors.toSet());
        final ConstraintViolationDto violationsDto = new ConstraintViolationDto(violations);
        return ResponseEntity
                .badRequest()
                .body(violationsDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ConstraintViolationDto> handleConstraintViolationException(final MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        Set<String> violations = ex.getFieldErrors().stream()
                .map(f -> f.getField() + " - " + f.getDefaultMessage())
                .collect(Collectors.toSet());
        final ConstraintViolationDto violationsDto = new ConstraintViolationDto(violations);
        return ResponseEntity
                .badRequest()
                .body(violationsDto);
    }

    private String getPropertyName(Path propertyPath) {
        PathImpl path = (PathImpl) propertyPath;
        return path.getLeafNode().getName();
    }
}
