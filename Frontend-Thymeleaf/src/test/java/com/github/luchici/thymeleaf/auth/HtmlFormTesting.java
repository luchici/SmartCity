package com.github.luchici.thymeleaf.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class HtmlFormTesting {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginSubmitValidUser() throws Exception {
        LoginUser expectedUser = new LoginUser("TestUsername", "TestPassword");
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("username", expectedUser.username)
                        .param("password", expectedUser.password))
                .andExpectAll(
                        status().isFound(),
                        view().name("redirect:/home"));
    }

    @Test
    void signupSubmitValidUser() throws Exception {
        NewUser expectedUser = new NewUser("Test Firstname", "Test Lastname", "TestUsername", "Test HomeCity", LocalDate.now(), "Test Email", "TestPassword");
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("firstname", expectedUser.firstName)
                        .param("lastname", expectedUser.lastName)
                        .param("username", expectedUser.username)
                        .param("homecity", expectedUser.homecity)
                        // .param("dob",expectedUser.dob.format(DateTimeFormatter.ofPattern("MM/dd/YYYY")))
                        .param("email", expectedUser.email)
                        .param("password", expectedUser.password))
                .andExpectAll(
                        status().isFound(),
                        view().name("redirect:/home"));
    }
}
