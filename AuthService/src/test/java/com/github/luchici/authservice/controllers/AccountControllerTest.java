package com.github.luchici.authservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.github.luchici.authservice.model.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AccountController.class,excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(ConfigBeans.class)
class AccountControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @Test
    void successfullyGetUserByUsername() throws Exception {
        String username = "ladyGaga";
        Account account = AccountBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        when(accountService.getAccountByUsername(username)).thenReturn(account);

        mockMvc.perform(get("/api/auth/users/{username}", username))
                .andExpectAll(
                        status().isOk(),
                        content().string(objectMapper.writeValueAsString(account)));
        verify(accountService).getAccountByUsername(username);
    }

    @Test
    void successfullyGetFirstPageOfAllUsers() throws Exception {
        List<ResponseAccountDto> usersList = List.of(ResponseAccountDtoBuilder.builder().withDefault().build(),
                ResponseAccountDtoBuilder.builder().withDefault().withRoles(ADMIN).build(),
                ResponseAccountDtoBuilder.builder().withDefault().build(),
                ResponseAccountDtoBuilder.builder().withDefault().build());
        Page<ResponseAccountDto> users = new PageImpl<>(usersList);
        when(accountService.getAllAccounts(1)).thenReturn(users);
        mockMvc.perform(get("/api/auth/users"))
                .andExpectAll(
                        status().isOk(),
                        content().string(objectMapper.writeValueAsString(users.toList())));
        verify(accountService).getAllAccounts(1);
    }

    @Test
    void successfullyUpdateAccount() throws Exception {
        String username = "ladyGaga";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        when(accountService.updateOrCreateAccount(username, newAccountDto)).thenReturn(false);
        mockMvc.perform(
                        put("/api/auth/users/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(
                        status().isOk());
        verify(accountService).updateOrCreateAccount(username, newAccountDto);
    }

    @Test
    void successfullyCreateAccountByHTTPPut() throws Exception {
        String username = "ladyGaga";
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        ResponseAccountDto expectedAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .withUsername(username)
                .build();
        when(accountService.updateOrCreateAccount(username, newAccountDto)).thenReturn(true);
        var response = mockMvc.perform(post("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ResponseAccountDto actualAccountDto = objectMapper.readValue(response.getContentAsString(), ResponseAccountDto.class);
        assertEquals(expectedAccountDto,actualAccountDto);
        verify(accountService).save(newAccountDto);
    }

    @Test
    void successfullyDeleteUserAccount() throws Exception {
        String username = "ladyGaga";
        mockMvc.perform(
                        delete("/api/auth/users/{username}", username))
                .andExpect(
                        status().isNoContent());
        verify(accountService).deleteAccount(username);
    }
}
