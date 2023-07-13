package com.github.luchici.authservice.model.dtos;

import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

class NewAccountDtoValidationTest {
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> rolesGenerator() {
        return Stream.of(
                Arguments.of(Collections.EMPTY_SET));
    }

    private NewAccountDto accountUnderTest;

    @BeforeEach
    void setUp() {
        accountUnderTest = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
    }

    @Test
    void successfullyCreateNewAccountDto() {
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, is(empty()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenUsernameNotValid(final String username) {
        // Username is not valid when empty, blank, null, less that 4 characters, more than 25 characters, contain other char than letter or digits
        accountUnderTest.setUsername(username);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenPasswordNotValid(String password) {
        // Password is not valid when empty, blank, null, more than 25 characters, contain other char than letter or digits
        accountUnderTest.setPassword(password);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenFirstNameNotValid(String firstName) {
        // FirstName is not valid when empty, blank, null, more than 25 characters, contain other char than letter or digits
        accountUnderTest.setFirstName(firstName);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenLastNameNotValid(String lastName) {
        // Username is not valid when empty, blank, null, more than 25 characters, contain other char than letter or digits
        accountUnderTest.setLastName(lastName);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void throwException_WhenHomecityNotValid(String homecity) {
        // Username is not valid when empty, blank, null, more than 25 characters, contain other char than letter or digits
        accountUnderTest.setHomecity(homecity);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "      ", "abc", "abcd@","abcd@gmail","abcd@gmail.", "abcd--dcba@","abcd--dcba@gmail","abcd--dcba@gmail."})
    void throwException_WhenEmailNotValid(String email) {
        accountUnderTest.setLastName(email);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"2025-10-31"})
    void throwException_WhenDobNotValid(LocalDate dob) {
        // Dob is not valid when is in the future oris the present date
        accountUnderTest.setDob(dob);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("rolesGenerator")
    void throwException_WhenRolesNotValid(Set<String> roles) {
        // Roles set is not valid when empty or null
        accountUnderTest.setRoles(roles);
        Set<ConstraintViolation<NewAccountDto>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }
}
