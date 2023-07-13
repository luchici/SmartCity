package com.github.luchici.authservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.builders.ResponseAccountDtoBuilder;
import com.github.luchici.authservice.config.ConfigBeans;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest(webEnvironment = RANDOM_PORT)
// @AutoConfigureMockMvc
@WebMvcTest(value = AuthController.class,excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(ConfigBeans.class)
class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginSuccessfully() throws Exception {
        LoginData loginData = new LoginData("TestName", "testPass");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpectAll(
                        status().isOk());
        verify(authService).login(loginData);
    }

    @Test
    void loginUnsuccessful() throws Exception {
        LoginData loginData = new LoginData("TestName", "testPass");
        doThrow(BadCredentialsException.class).when(authService).login(loginData);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData))
                        .with(csrf()))
                .andExpectAll(
                        status().isUnauthorized());
        verify(authService).login(loginData);
    }

    @Test
    void registerValidUser() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        ResponseAccountDto expectedAccountDto = ResponseAccountDtoBuilder.builder()
                .withDefault()
                .build();
        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated(),
                        header().string(CONTENT_LOCATION, "/api/auth/user/" + newAccountDto.getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        ResponseAccountDto actualAccountDto = objectMapper.readValue(response.getContentAsString(), ResponseAccountDto.class);
        assertEquals(expectedAccountDto,actualAccountDto);
        verify(authService).register(newAccountDto);
    }
}
