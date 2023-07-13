package com.github.luchici.cityservice.model.dtos.response;

import java.util.Set;

public interface CityResponseDto {
    String getCityName();

    String getCountry();

    Set<AttractionResponseDto> getAttractions();

    // Set<ImageDataByCity> getImages();
}
