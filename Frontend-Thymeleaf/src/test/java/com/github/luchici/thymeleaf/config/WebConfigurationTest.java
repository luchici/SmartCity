package com.github.luchici.thymeleaf.config;

import com.github.luchici.thymeleaf.model.City;
import com.github.luchici.thymeleaf.auth.AuthService;
import com.github.luchici.thymeleaf.home.HomeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.emptyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class WebConfigurationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HomeService homeService;
    @MockBean
    private AuthService authService;
    @Test
    void getHomeEndpointReturns200Ok() throws Exception {
        Mockito.when(homeService.getCities()).thenReturn(emptyList());
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void getCityByNameEndpointReturns200Ok() throws Exception {
        Mockito.when(homeService.getCity("Madrid")).thenReturn(new City());
        mockMvc.perform(get("/cities/{cityName}", "Madrid"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoginEndpointReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void postTologinEndpointReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void getSignUpEndpointReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @Test
    void postToSignUpEndpointReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

}
