package com.github.luchici.thymeleaf.openfeign;

import com.github.luchici.thymeleaf.model.City;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// TODO: Need to be refactoring
@FeignClient(name="city",url = "localhost:8083")
public interface CityServiceClient {

    @GetMapping
    City getCity(String cityName);

    @GetMapping
    List<City> getCities();
}
