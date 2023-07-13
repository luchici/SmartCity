package com.github.luchici.thymeleaf.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class City {

    public String cityName;
    public String country;
    public Set<ImageData> image;
    public Set<Attraction> attractions;

    public City(String cityName, String country) {
        this.cityName = cityName;
        this.country = country;
    }
}
