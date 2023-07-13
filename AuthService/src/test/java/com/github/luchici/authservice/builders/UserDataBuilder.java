package com.github.luchici.authservice.builders;

import com.github.luchici.authservice.model.embedded.UserData;

import java.time.LocalDate;

public class UserDataBuilder {
    private static UserDataBuilder userDataBuilder;
    private String firstName;
    private String lastName;
    private String homecity;
    private LocalDate dob;
    private String email;

    public static UserDataBuilder builder() {
        userDataBuilder = new UserDataBuilder();
        return userDataBuilder;
    }

    public UserDataBuilder withDefault() {
        this.firstName = "David";
        this.lastName = "Abdullah";
        this.homecity = "Jerusalem";
        this.dob = LocalDate.of(1977, 07, 23);
        this.email = "david@gmail.com";
        return userDataBuilder;
    }

    public UserDataBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return userDataBuilder;
    }

    public UserDataBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return userDataBuilder;
    }

    public UserDataBuilder withHomecity(String homecity) {
        this.homecity = homecity;
        return userDataBuilder;
    }

    public UserDataBuilder withDob(LocalDate dob) {
        this.dob = dob;
        return userDataBuilder;
    }

    public UserDataBuilder withEmail(String email) {
        this.email = email;
        return userDataBuilder;
    }

    public UserData build() {
        return new UserData(
                this.firstName,
                this.lastName,
                this.homecity,
                this.dob,
                this.email);
    }
}
