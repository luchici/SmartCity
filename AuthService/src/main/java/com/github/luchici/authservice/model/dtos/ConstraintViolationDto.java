package com.github.luchici.authservice.model.dtos;

import lombok.Value;

import java.util.Set;
import java.util.stream.Collectors;

@Value
public class ConstraintViolationDto {
    Set<String> violations;

    public ConstraintViolationDto(final Set<String> violations) {
        this.violations = violations.stream()
                .map(v->"Invalid "+v)
                .collect(Collectors.toSet());
    }
}
