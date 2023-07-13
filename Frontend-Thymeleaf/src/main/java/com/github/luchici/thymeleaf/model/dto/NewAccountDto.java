package com.github.luchici.thymeleaf.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAccountDto {
    public String firstName;
    public String lastName;
    public String username;
    public String homecity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate dob;
    public String email;
    public String password;
}
