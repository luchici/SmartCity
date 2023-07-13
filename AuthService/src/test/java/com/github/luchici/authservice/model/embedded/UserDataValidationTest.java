package com.github.luchici.authservice.model.embedded;

import com.github.luchici.authservice.builders.UserDataBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

class UserDataValidationTest {
    private static Validator validator;
    private UserData userDataUnderTest;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        userDataUnderTest=UserDataBuilder.builder()
                .withDefault()
                .build();
    }

    @Test
    void successfullyCreateUserData() {
        UserData userDataUnderTest = UserDataBuilder.builder()
                .withDefault()
                .build();
        Set<ConstraintViolation<UserData>> violations = validator.validate(userDataUnderTest);
        assertThat(violations, is(empty()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenFirstNameNotValid(String firstName) {
        userDataUnderTest.setFirstName(firstName);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userDataUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenLastNameNotValid(String lastName) {
        userDataUnderTest.setLastName(lastName);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userDataUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenHomecityNotValid(String homecity) {
        userDataUnderTest.setHomecity(homecity);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userDataUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"2025-10-31"})
    void throwException_WhenDobNotValid(LocalDate dob) {
        userDataUnderTest.setDob(dob);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userDataUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "abcd@","abcd@gmail","abcd@gmail.", "abcd--dcba@","abcd--dcba@gmail","abcd--dcba@gmail."})
    void throwException_WhenEmailNotValid(String email) {
        userDataUnderTest.setEmail(email);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userDataUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }
}
