package com.github.luchici.authservice.model.dtos;

import com.github.luchici.authservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("account")
public class ResponseAccountDto {
    private String username;
    private Set<Role> roles;
    private String firstName;
    private String lastName;
    private String homecity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    private String email;
}
