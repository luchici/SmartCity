package com.github.luchici.authservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.model.dtos.ConstraintViolationDto;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.services.AccountService;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AccountController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(ConfigBeans.class)
class AccountControllerValidationTest {
    private static final String STRING_INPUT_VALIDATION_ERROR = "please use just letters and digits, no less than 4 characters and no more that 25 characters";
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;

    @ParameterizedTest
    @ValueSource(strings = {"      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void getUserByUsername_ThrowConstraintViolationException_WhenInvalidParameter(final String username) throws Exception {
        ConstraintViolationDto expectedViolations = new ConstraintViolationDto(
                Set.of("username - " + STRING_INPUT_VALIDATION_ERROR));
        mockMvc.perform(get("/api/auth/users/{username}", username))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void getAllUsers_ThrowMethodArgumentNotValidException_WhenInvalidParameter(final int pageNumber) throws Exception {
        final String violationMessage = "pageNumber - the min value is 1";
        ConstraintViolationDto expectedViolations =
                new ConstraintViolationDto(Set.of(violationMessage));
        mockMvc.perform(get("/api/auth/users")
                        .param("pageNumber", String.valueOf(pageNumber)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @Test
    void createNewUser_ThrowMethodArgumentNotValidException_WhenInvalidParameter() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername("asced----adsas")
                .withEmail(null)
                .build();
        ConstraintViolationDto expectedViolations = new ConstraintViolationDto(getStringViolations(newAccountDto));
        mockMvc.perform(post("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @Test
    void updateOrCreateUser_ThrowConstraintViolationException_WhenInvalidUsernameAndNewAccountDto() throws Exception {
        String username = "asced----adsas";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        ConstraintViolationDto expectedViolations =
                new ConstraintViolationDto(Set.of("username - "+STRING_INPUT_VALIDATION_ERROR));
        mockMvc.perform(put("/api/auth/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @Test
    void updateOrCreateUser_ThrowConstraintViolationException_WhenInvalidNewAccountDto() throws Exception {
        String invalidUsername = "asced----adsas";
        String validUsername = "someValidUsername";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(invalidUsername)
                .build();
        ConstraintViolationDto expectedViolations =
                new ConstraintViolationDto(Set.of("username - "+STRING_INPUT_VALIDATION_ERROR));
        mockMvc.perform(put("/api/auth/users/{username}", validUsername)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"      ", "abc", "moreThat25CharactersInAString", "abcd--dcba"})
    void deleteUser(final String username) throws Exception {
        ConstraintViolationDto expectedViolations = new ConstraintViolationDto(
                Set.of("username - " + STRING_INPUT_VALIDATION_ERROR));
        mockMvc.perform(delete("/api/auth/users/{username}", username))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    private Set<String> getStringViolations(NewAccountDto newAccountDto) {
        var constraintViolations = validator.validate(newAccountDto);
        return constraintViolations.stream()
                .map(c -> getPropertyName(c.getPropertyPath()) + " - " + c.getMessage())
                .collect(Collectors.toSet());
    }

    private String getPropertyName(Path propertyPath) {
        PathImpl path = (PathImpl) propertyPath;
        String a = path.getLeafNode().getName();
        return a;
    }
}
