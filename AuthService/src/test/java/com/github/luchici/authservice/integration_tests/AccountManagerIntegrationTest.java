package com.github.luchici.authservice.integration_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers()
@ActiveProfiles("test-containers")
class AccountManagerIntegrationTest {
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

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyGetAccountByUsername() throws Exception {
        // TODO: Check roles
        Account account = AccountBuilder.builder()
                .withDefault()
                .build();
        account.setId(26);
        var response = mockMvc.perform(get("/api/auth/users/{username}", account.getUsername()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Account responseAccount = objectMapper.readValue(response.getContentAsString(),Account.class);
        assertTrue(account.fullyEquals(responseAccount));
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql("classpath:testdata/clean_database.sql")
    void failsToGetAccountByUsername_WhenUsernameNotInDatabase() throws Exception {
        mockMvc.perform(get("/api/auth/users/{username}", "someUniqueUsername"))
                .andExpectAll(
                        status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyGetSecondPageOfUsersInDatabase() throws Exception {
        var response = mockMvc.perform(get("/api/auth/users")
                        .param("pageNumber", "2"))
                .andExpect(
                        status().isOk())
                .andReturn().getResponse();
        List<ResponseAccountDto> responseAccountList =
                objectMapper.readValue(response.getContentAsString(), new TypeReference<List<ResponseAccountDto>>() {
                });
        Map<String, String> headers = response.getHeaderNames().stream().collect(Collectors.toMap(
                headerName -> headerName,
                headerName -> response.getHeader(headerName)));
        assertAll(
                () -> assertEquals(5, responseAccountList.size()),
                () -> assertTrue(headers.containsKey("Total Pages")),
                () -> assertTrue(headers.containsKey("Total Elements")),
                () -> assertEquals(6, Integer.valueOf(headers.get("Total Pages"))),
                () -> assertEquals(26, Integer.valueOf(headers.get("Total Elements")))
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    void getAnEmptyPageWhenNoAccountsInDatabase() throws Exception {
        var response = mockMvc.perform(get("/api/auth/users")
                        .param("pageNumber", "1"))
                .andExpect(
                        status().isOk())
                .andReturn().getResponse();
        List<ResponseAccountDto> responseAccountList =
                objectMapper.readValue(response.getContentAsString(), new TypeReference<List<ResponseAccountDto>>() {
                });
        Map<String, String> headers = response.getHeaderNames().stream().collect(Collectors.toMap(
                headerName -> headerName,
                headerName -> response.getHeader(headerName)));
        assertAll(
                () -> assertEquals(0, responseAccountList.size()),
                () -> assertTrue(headers.containsKey("Total Pages")),
                () -> assertTrue(headers.containsKey("Total Elements")),
                () -> assertEquals(0, Integer.valueOf(headers.get("Total Pages"))),
                () -> assertEquals(0, Integer.valueOf(headers.get("Total Elements")))
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    void successfullyCreateNewUser() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        ResponseAccountDto expectedAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .build();
        var response = mockMvc.perform(post("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ResponseAccountDto actualAccountDto = objectMapper.readValue(response.getContentAsString(), ResponseAccountDto.class);

        Map<String, String> headers = response.getHeaderNames().stream().collect(Collectors.toMap(
                headerName -> headerName,
                headerName -> response.getHeader(headerName)));

        String expectedCreatedHeader = "/api/auth/users/" + newAccountDto.getUsername();

        assertAll(
                () -> assertTrue(headers.containsKey("Location")),
                () -> assertEquals(expectedCreatedHeader, response.getHeader("Location")),
                () -> assertEquals(expectedAccountDto, actualAccountDto)
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void failsToCreateNewUser_WhenUsernameAndEmailAlreadyExists() throws Exception {
        // TODO: Update when exceptions
        NewAccountDto accountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        var response = mockMvc.perform(post("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(
                        status().isBadRequest())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyUpdateExistingUser() throws Exception {
        String updateUsername = "someUniqueUsername";
        String updateHomecity = "Craiova";
        String updateAccount = "DavidTheBeast";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(updateUsername)
                .withHomecity(updateHomecity)
                .build();
        ResponseAccountDto expectedResponseAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(updateUsername)
                .withHomecity(updateHomecity)
                .build();
        var response = mockMvc.perform(put("/api/auth/users/{username}",updateAccount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ResponseAccountDto actualResponseAccountDto = objectMapper.readValue(response.getContentAsString(),ResponseAccountDto.class);

        assertAll(
                ()->assertEquals(expectedResponseAccountDto,actualResponseAccountDto));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    void successfullyCreateNewUserByHTTPPut() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        ResponseAccountDto expectedAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .build();
        var response = mockMvc.perform(put("/api/auth/users/{username}",newAccountDto.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ResponseAccountDto actualAccount = objectMapper.readValue(response.getContentAsString(), ResponseAccountDto.class);
        assertEquals(expectedAccountDto,actualAccount);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("classpath:testdata/clean_database.sql")
    @Sql("classpath:testdata/insert_accounts_with_default.sql")
    void successfullyDeleteUser() throws Exception {
        NewAccountDto accountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        var response = mockMvc.perform(delete("/api/auth/users/{username}",accountDto.getUsername()))
                .andExpect(
                        status().isNoContent());
    }
}
