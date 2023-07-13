package com.github.luchici.authservice.builders;

import com.github.luchici.authservice.model.Role;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;

public class ResponseAccountDtoBuilder {
    private static ResponseAccountDtoBuilder responseAccountDtoBuilder;
    private String username;
    private Set<Role> roles;
    private String firstName;
    private String lastName;
    private String homecity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    private String email;

    public static ResponseAccountDtoBuilder builder() {
        responseAccountDtoBuilder = new ResponseAccountDtoBuilder();
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withDefault() {
        this.username = "DavidTheBeast";
        this.roles = Set.of(ADMIN,USER);
        this.firstName = "David";
        this.lastName = "Abdullah";
        this.homecity = "Jerusalem";
        this.dob = LocalDate.of(1977, 07, 23);
        this.email = "david@gmail.com";
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withUsername(String username) {
        this.username = username;
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withRoles(Role... roles) {
        this.roles = Set.of(roles);
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withHomecity(String homecity) {
        this.homecity = homecity;
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withDob(LocalDate dob) {
        this.dob = dob;
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDtoBuilder withEmail(String email) {
        this.email = email;
        return responseAccountDtoBuilder;
    }

    public ResponseAccountDto build() {
        return new ResponseAccountDto(
                this.username,
                this.roles,
                this.firstName,
                this.lastName,
                this.homecity,
                this.dob,
                this.email);
    }
}
