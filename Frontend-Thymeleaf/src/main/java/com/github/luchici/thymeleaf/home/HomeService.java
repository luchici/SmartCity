package com.github.luchici.thymeleaf.home;

import com.github.luchici.thymeleaf.model.City;
import com.github.luchici.thymeleaf.openfeign.AuthServiceClient;
import com.github.luchici.thymeleaf.openfeign.CityServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final AuthServiceClient authServiceClient;
    private final CityServiceClient cityServiceClient;


    public List<City> getCities() {
        return cityServiceClient.getCities();
    }

    public City getCity(String cityName) {
    return cityServiceClient.getCity(cityName);
        }
}
