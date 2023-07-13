package com.github.luchici.thymeleaf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.thymeleaf.home.HomeService;
import com.github.luchici.thymeleaf.model.City;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeServiceTest {
    public static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HomeService homeService;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        var httpUrl = mockWebServer.url("/cities");
        var webClient = WebClient.create(httpUrl.toString());
        homeService = new HomeService(webClient);
    }

    @Test
    void getCities() throws JsonProcessingException {
        // Given
        List<City> expectedCities = List.of(
                new City("Craiova", "Romania"),
                new City("Warsaw", "Poland"),
                new City("Dublin", "Ireland"));
        var response = new MockResponse()
                .setBody(objectMapper.writeValueAsString(expectedCities))
                .addHeader("Content-Type", "application/json");
        // When
        mockWebServer.enqueue(response);
        List<City> actualCities = homeService.getCities();
        // Then
        assertEquals(expectedCities,actualCities);
    }

    @Test
    void getCity() throws JsonProcessingException {
        // Given
        City expectedCity = new City("Madrid", "Spain");
        var response = new MockResponse()
                .setBody(objectMapper.writeValueAsString(expectedCity))
                .addHeader("Content-Type", "application/json");
        // When
        mockWebServer.enqueue(response);
        City actualCity = homeService.getCity("Madrid");
        // Then
        assertEquals(expectedCity,actualCity);
    }
}
