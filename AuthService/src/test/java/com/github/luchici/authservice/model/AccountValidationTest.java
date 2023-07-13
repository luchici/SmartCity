package com.github.luchici.authservice.model;

import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.model.embedded.AccountState;
import com.github.luchici.authservice.model.embedded.LoginData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static com.github.luchici.authservice.model.Role.USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

// @SpringBootTest
// @ExtendWith(SpringExtension.class)
// @ContextConfiguration(classes = {LocalValidatorFactoryBean.class})
// @Import(ValidationAutoConfiguration.class)
class AccountValidationTest {
    private static Validator validator;
    private static ExecutableValidator executableValidator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        executableValidator = factory.getValidator().forExecutables();
    }

    private static Stream<Arguments> generateValidRoles() {
        return Stream.of(
                Arguments.of(Set.of(ADMIN)),
                Arguments.of(Set.of(USER)),
                Arguments.of(Set.of(USER, ADMIN)));
    }

    private static Stream<Arguments> generateNotValidLoginData() {
        return Stream.of(
                Arguments.of(new LoginData("", "12345")),
                Arguments.of(new LoginData("         ", "12345")),
                Arguments.of(new LoginData("abc", "12345")),
                Arguments.of(new LoginData("moreThat25CharactersInAString", "12345")),
                Arguments.of(new LoginData("abcd--dcba", "12345")));
    }

    private static Stream<Arguments> generateNotValidAccountState() {
        return Stream.of(
                Arguments.of(new AccountState(null, true, true, true)),
                Arguments.of(new AccountState(false, null, true, true)),
                Arguments.of(new AccountState(false, false, null, true)),
                Arguments.of(new AccountState(false, false, false, null)),
                Arguments.of(new AccountState(false, false, null, null)));
    }

    private Account accountUnderTest;

    @BeforeEach
    void setUp() {
        accountUnderTest = AccountBuilder.builder()
                .withDefault()
                .build();
    }

    @Test
    void successfullyAccount() {
        Set<ConstraintViolation<Account>> violations = validator.validate(accountUnderTest);
        violations.stream().forEach(System.out::println);
        assertThat(violations, is(empty()));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("generateNotValidLoginData")
    void throwException_WhenLoginDataNotValid(LoginData loginData) {
        accountUnderTest.setLoginData(loginData);
        Set<ConstraintViolation<Account>> violations = validator.validate(accountUnderTest);
        violations.stream().forEach(System.out::println);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    void throwException_WhenRolesIsEmpty() {
        accountUnderTest.setRoles(Collections.EMPTY_SET);
        Set<ConstraintViolation<Account>> violations = validator.validate(accountUnderTest);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    void throwException_WhenRolesIsNull() throws NoSuchMethodException {
        // accountUnderTest.setRoles(null);
        Account accountUnderTest = new Account();
        Method methodUnderTest = Account.class.getMethod("setRoles", Collection.class);
        Object[] parameterValues = {null};
        Set<ConstraintViolation<Account>> violations
                = executableValidator.validateParameters(accountUnderTest, methodUnderTest, parameterValues);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("generateNotValidAccountState")
    void throwException_WhenAccountStateNotValid(AccountState accountState) {
        accountUnderTest.setState(accountState);
        Set<ConstraintViolation<Account>> violations = validator.validate(accountUnderTest);
        violations.stream().forEach(System.out::println);
        assertThat(violations, hasSize(greaterThanOrEqualTo(1)));
    }
}
