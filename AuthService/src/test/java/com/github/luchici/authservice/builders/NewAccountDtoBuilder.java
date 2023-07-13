package com.github.luchici.authservice.builders;

import com.github.luchici.authservice.model.dtos.NewAccountDto;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;

@ToString
public class NewAccountDtoBuilder {
    private static NewAccountDtoBuilder newAccountDtoBuilder;

    public static NewAccountDtoBuilder builder() {
        newAccountDtoBuilder = new NewAccountDtoBuilder();
        return newAccountDtoBuilder;
    }
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String homecity;
    private LocalDate dob;
    private String email;
    private Set<String> roles;

    public NewAccountDtoBuilder withDefault() {
        this.username = "DavidTheBeast";
        this.password = "12345";
        this.roles = Set.of(ADMIN.name(),USER.name());
        this.firstName = "David";
        this.lastName = "Abdullah";
        this.homecity = "Jerusalem";
        this.dob = LocalDate.of(1977, 07, 23);
        this.email = "david@gmail.com";
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withUsername(String username) {
        this.username = username;
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withHomecity(String homecity) {
        this.homecity = homecity;
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withDob(LocalDate dob) {
        this.dob = dob;
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withEmail(String email) {
        this.email = email;
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withRole(String... roles) {
        this.roles = Set.of(roles);
        return newAccountDtoBuilder;
    }

    public NewAccountDtoBuilder withPassword(String password) {
        this.password = password;
        return newAccountDtoBuilder;
    }

    public NewAccountDto build() {
        return new NewAccountDto(
                this.username, this.password,
                this.firstName, this.lastName, this.homecity, this.dob, this.email,
                this.roles);
    }
}
