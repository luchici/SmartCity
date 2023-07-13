package com.github.luchici.cityservice.model.dtos.request;

import com.github.luchici.cityservice.model.entities.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AttractionRequestDto {
    String name;
    City city;
}
