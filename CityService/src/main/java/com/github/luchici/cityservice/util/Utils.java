package com.github.luchici.cityservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luchici.cityservice.model.dtos.response.CityResponseDto;
import com.github.luchici.cityservice.model.entities.City;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static String insertCity = "INSERT INTO cities(city_name,country) VALUES(?,?,?)";
    public static String countRowsCities = "SELECT COUNT(*) AS COUNT FROM cities";
    public static String selectCityIdByName = "SELECT id FROM cities WHERE city_name=?";
    public static String selectCityByName = "SELECT * FROM cities WHERE city_name=?";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String capitalizeWords(String cityName) {
        return Arrays.stream(cityName.split(" "))
                .map(Utils::capitalizeWord)
                .collect(Collectors.joining(" "));
    }

    public static String capitalizeWord(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    public static City formJsonStringToCity(final String cityAsString) {
        try {
            return objectMapper.readValue(cityAsString, City.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CityResponseDto formJsonStringToCityResponseDto(final String cityAsString) {
        try {
            return objectMapper.readValue(cityAsString, CityResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formCityToJsonString(final City city) {
        try {
            return objectMapper.writeValueAsString(city);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
