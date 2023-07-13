package com.github.luchici.cityservice.exception;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
class ValidationExceptionDto {
    String message;
    String field;
    String invalidValue;
}
