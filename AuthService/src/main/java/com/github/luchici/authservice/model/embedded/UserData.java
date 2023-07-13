package com.github.luchici.authservice.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private static final String STRING_INPUT_VALIDATION_ERROR = "Please use just letters and digits, no less than 4 and no more that 25 characters";
    private static final String DIGITS_AND_LETTERS = "^([A-Za-z0-9]{4,25})$";
    @NotBlank
    @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR)
    private String firstName;
    @NotBlank
    @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR)
    private String lastName;
    @NotBlank
    @Pattern(regexp = DIGITS_AND_LETTERS, message = STRING_INPUT_VALIDATION_ERROR)
    private String homecity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    @Past(message = "The date of birth need to be in the past")
    private LocalDate dob;
    @NotBlank
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
}
