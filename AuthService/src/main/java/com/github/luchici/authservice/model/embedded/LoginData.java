package com.github.luchici.authservice.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData {
    private static final String STRING_INPUT_VALIDATION_ERROR = "please use just letters and digits, no less than 4 characters and no more that 25 characters";
    private static final String DIGITS_AND_LETTERS = "^([A-Za-z0-9]{4,25})$";
    @NotBlank
    @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR)
    private String username;
    @NotBlank
    private String password;
}
