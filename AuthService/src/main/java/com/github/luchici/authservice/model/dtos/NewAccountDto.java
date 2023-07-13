package com.github.luchici.authservice.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountDto {
    private static final String STRING_INPUT_VALIDATION_ERROR = "please use just letters and digits, no less than 4 characters and no more that 25 characters";
    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9]{4,25})$", message = STRING_INPUT_VALIDATION_ERROR)
    private String username;
    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9]{4,25})$", message = STRING_INPUT_VALIDATION_ERROR)
    private String password;
    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9]{4,25})$", message = STRING_INPUT_VALIDATION_ERROR)
    private String firstName;
    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9]{4,25})$", message = STRING_INPUT_VALIDATION_ERROR)
    private String lastName;
    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9]{4,25})$", message = STRING_INPUT_VALIDATION_ERROR)
    private String homecity;
    @NotNull
    @Past(message = "The date of birth need to be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    @NotBlank
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
    @NotEmpty
    private Set<String> roles;
}
