package com.github.luchici.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.authservice.builders.NewAccountDtoBuilder;
import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.services.AccountService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountControllerSecurityEndpointsTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void users_EndpointReturn200_When_HTTPGet_WithAdmin() throws Exception {
        when(accountService.getAllAccounts(1)).thenReturn(Page.empty());
        mockMvc.perform(get("/api/auth/users"))
                .andExpectAll(
                        status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void users_EndpointReturn403_When_HTTPGet_WithUser() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpectAll(
                        status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void users_EndpointReturn200_When_HTTPPost_WithAdmin() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        mockMvc.perform(post("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void users_EndpointReturn403_When_HTTPPost_WithUser() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        mockMvc.perform(post("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void username_EndpointReturn201_When_HTTPut_WithAdmin() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        when(accountService.updateOrCreateAccount("adminUsername", newAccountDto)).thenReturn(true);
        mockMvc.perform(put("/api/auth/users/{username}","adminUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void username_EndpointReturn200_When_HTTPut_WithAdmin() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        when(accountService.updateOrCreateAccount("adminUsername", newAccountDto)).thenReturn(false);
        mockMvc.perform(put("/api/auth/users/{username}","adminUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isOk());
    }

    @Test
    @WithMockUser(username = "sophiaLoren", roles = "ADMIN")
    void username_EndpointReturn200_When_HTTPut_WithUserUpdateHisData() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        when(accountService.updateOrCreateAccount(user.getUsername(), newAccountDto)).thenReturn(false);
        mockMvc.perform(put("/api/auth/users/{username}",user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isOk());
    }

    @Test
    @WithMockUser(username = "sophiaLoren", roles = "USER")
    void username_EndpointReturn403_When_HTTPut_WithUserUpdateHisData() throws Exception {
        NewAccountDto newAccountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        mockMvc.perform(put("/api/auth/users/{username}","wrongUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpectAll(
                        status().isForbidden());
    }

    // /api/auth/users/{username} DELETE - just ADMIN
    @Test
    @WithMockUser(roles = "ADMIN")
    void username_EndpointReturn204_When_HTTPut_WithAdmin() throws Exception {
        mockMvc.perform(delete("/api/auth/users/{username}","adminUsername"))
                .andExpectAll(
                        status().isNoContent());
    }

    // /api/auth/users/{username} DELETE - just ADMIN
    @Test
    @WithMockUser(roles = "USER")
    void username_EndpointReturn403_When_HTTDelete_WithOtherUser() throws Exception {
        NewAccountDto accountDto = NewAccountDtoBuilder.builder()
                .withDefault()
                .build();
        mockMvc.perform(delete("/api/auth/users/{username}","adminUsername"))
                .andExpectAll(
                        status().isForbidden());
    }

    // /api/auth/users/{username} DELETE - the login user
    @Test
    @WithMockUser(username = "sophiaLoren", roles = "ADMIN")
    void username_EndpointReturn204_When_HTTDelete_WithUserDeleteHisData() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mockMvc.perform(delete("/api/auth/users/{username}",user.getUsername()))
                .andExpectAll(
                        status().isNoContent());
    }

    // /api/auth/users/{username} DELETE - the login user
    @Test
    @WithMockUser(username = "sophiaLoren", roles = "USER")
    void username_EndpointReturn403_When_HTTDelete_WithWrongUserDeleteHisData() throws Exception {

        mockMvc.perform(delete("/api/auth/users/{username}","wrongUsername"))
                .andExpectAll(
                        status().isForbidden());
    }
}
