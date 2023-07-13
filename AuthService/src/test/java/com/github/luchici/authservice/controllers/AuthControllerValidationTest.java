package com.github.luchici.authservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.model.dtos.ConstraintViolationDto;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.services.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(ConfigBeans.class)
class AuthControllerValidationTest {
    private static final String STRING_INPUT_VALIDATION_ERROR = "please use just letters and digits, no less than 4 characters and no more that 25 characters";
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @MockBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_throwConstraintViolationException_WhenInvalidUsername() throws Exception {
        String invalidUsername = "invalidUserName---------";
        LoginData loginData = new LoginData(invalidUsername, "validPassword");
        ConstraintViolationDto expectedViolations = new ConstraintViolationDto(
                Set.of("username - " + STRING_INPUT_VALIDATION_ERROR));
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @Test
    void login_throwConstraintViolationException_WhenPasswordIsNull() throws Exception {
        String nullPassword = null;
        LoginData loginData = new LoginData("validUsername", nullPassword);
        ConstraintViolationDto expectedViolations = new ConstraintViolationDto(
                Set.of("password - " + "must not be blank"));
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }

    @Test
    void register_throwConstraintViolationException_WhenInvalidNewAccount() throws Exception {
        String invalidUsername = "invalidUsername-----";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(invalidUsername)
                .build();
        ConstraintViolationDto expectedViolations = new ConstraintViolationDto(
                Set.of("username - " + STRING_INPUT_VALIDATION_ERROR));
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(expectedViolations)));
    }
}
