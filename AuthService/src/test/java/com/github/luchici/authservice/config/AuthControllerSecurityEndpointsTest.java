package com.github.luchici.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.authflow.UserDetailsServiceImpl;
import com.github.luchici.authservice.builders.AccountBuilder;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.model.Account;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(value = AuthController.class)
// @Import({SecurityConfig.class,UserDetailsServiceImpl.class})
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerSecurityEndpointsTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginEndpointIsAccessibleToEverybodyAndReturn200_WhenSuccessfullyLogIn() throws Exception {
        Account account = AccountBuilder.builder()
                .withDefault()
                .build();
        when(userDetailsService.loadUserByUsername(account.getUsername())).thenReturn(account);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account.getLoginData())))
                .andExpect(
                        status().isOk());
    }

    @Test
    void loginEndpointfailLogin() throws Exception {
        Account account = AccountBuilder.builder()
                .withDefault()
                .build();
        when(userDetailsService.loadUserByUsername(account.getUsername())).thenReturn(account);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account.getLoginData())))
                .andExpect(
                        status().isOk());
    }

    @Test
    void registerEndpointIsAccessibleToEverybody() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated());
    }
}
