package com.github.luchici.cityservice.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class CityRequestDto {

    String cityName;
    String country;
    Set<AttractionRequestDto> attractions;

}
