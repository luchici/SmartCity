package com.github.luchici.authservice.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.model.dtos.ExceptionResponseDto;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.model.embedded.LoginData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test-containers")
class AuthIntegrationTest {
    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14-alpine");

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyLogIn() throws Exception {
        LoginData loginData = new LoginData("DavidTheBeast", "12345");
        var response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpectAll(
                        status().isOk(),
                        content().string("Success"));
    }

    @Test
    void failToLogIn_WhenUsernameNotInDatabase() throws Exception {
        String uniquePassword = "879456";
        String uniqueUsername = "someUniqueUsername";
        LoginData loginData = new LoginData(uniqueUsername, uniquePassword);
        ExceptionResponseDto exceptionDto = new ExceptionResponseDto("Incorrect password or username. Please try again");
        var response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(exceptionDto)));
    }

    @Test
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void failToLogIn_WhenWrongPassword() throws Exception {
        String uniquePassword = "879456";
        LoginData loginData = new LoginData("DavidTheBeast", uniquePassword);
        ExceptionResponseDto exceptionDto = new ExceptionResponseDto("Incorrect password or username. Please try again");
        var response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(exceptionDto)));
    }

    @Test
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    @Sql("classpath:testdata/update_autoincrement.sql")
    void successfullyRegisterNewUser() throws Exception {
        String uniqueUsername = "someUniqueUsername";
        String uniqueEmail = "someUniqueEmail@gmail.com";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(uniqueUsername)
                .withEmail(uniqueEmail)
                .build();
        ResponseAccountDto expectedAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(uniqueUsername)
                .withEmail(uniqueEmail)
                .build();
        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated(),
                        header().string(CONTENT_LOCATION, "/api/auth/user/" + uniqueUsername),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ResponseAccountDto actualAcccccountDto = objectMapper.readValue(response.getContentAsString(), ResponseAccountDto.class);
        assertEquals(expectedAccountDto,actualAcccccountDto);
    }

    @Test
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    @Sql("classpath:testdata/update_autoincrement.sql")
    void failToRegisterNewUser_WhenUsernameExistInDatabase() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();

        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(
                        status().isBadRequest())
                .andReturn().getResponse();

        assertAll(
                () -> assertTrue(response.getContentAsString().contains("username"))
        );
    }

    @Test
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    @Sql("classpath:testdata/update_autoincrement.sql")
    void failToRegisterNewUser_WhenEmailExistInDatabase() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername("someUniqueUsername")
                .build();

        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(
                        status().isBadRequest())
                .andReturn().getResponse();

        assertAll(
                () -> assertTrue(response.getContentAsString().contains("email"))
        );
    }
}
