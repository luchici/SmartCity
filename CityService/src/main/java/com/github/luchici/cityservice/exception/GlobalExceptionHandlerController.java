package com.github.luchici.cityservice.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {
    private final MessageSource messageSource;

    @Nullable
    private static Object[] getParams(ConstraintViolationException validationExceptions) {
        Object[] params = null;
        if (validationExceptions.getConstraintViolations().size() > 1) {
            params = new Integer[]{validationExceptions.getConstraintViolations().size()};
        }
        return params;
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Exception> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception);
    }

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<ApiException> handleApiBaseException(ApiException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<String> constraintViolationException(ConstraintViolationException validationExceptions, HttpServletRequest request) {
        // Object[] params = getParams(validationExceptions);
        // Set<ValidationExceptionDto> errors = getValidationExceptionDtos(validationExceptions);
        // logConstraintViolationExceptions(params, errors);
        // Map<String, Object> body = formatBodyConstraintViolationExceptions(request, params, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some test here");
    }

    @NonNull
    private Map<String, Object> formatBodyConstraintViolationExceptions(HttpServletRequest request, Object[] params, Set<ValidationExceptionDto> errors) {
        String userMessage = messageSource.getMessage("exception.VALIDATION_EXCEPTION", params, request.getLocale());
        Map<String, Object> body = Map.of(
                messageSource.getMessage("Message", null, request.getLocale()), userMessage,
                "errors", errors);
        return body;
    }

    private void logConstraintViolationExceptions(Object[] params, Set<ValidationExceptionDto> errors) {
        String logMessage = messageSource.getMessage("exception.VALIDATION_EXCEPTION", params, Locale.getDefault());
        log.error(logMessage);
        errors.stream().forEach(e -> log.error(e.toString()));
    }

    @NonNull
    private Set<ValidationExceptionDto> getValidationExceptionDtos(ConstraintViolationException validationExceptions) {
        Set<ValidationExceptionDto> errors = validationExceptions.getConstraintViolations()
                .stream().map(c ->
                        new ValidationExceptionDto(
                                c.getMessage(),
                                getFieldFromPath(c.getPropertyPath()),
                                String.valueOf(c.getInvalidValue())))
                .collect(Collectors.toSet());
        System.out.println("==============================");
        validationExceptions.getConstraintViolations().forEach(c -> System.out.println(c));
        validationExceptions.getConstraintViolations().forEach(c -> System.out.println(c.getMessageTemplate()));
        validationExceptions.getConstraintViolations().forEach(c -> System.out.println(c.getMessage()));
        validationExceptions.getConstraintViolations().forEach(c -> System.out.println(c.getRootBeanClass()));
        return errors;
    }

    private String getFieldFromPath(Path fieldPath) {
        PathImpl pathImpl = (PathImpl) fieldPath;
        return pathImpl.getLeafNode().toString();
    }
}
