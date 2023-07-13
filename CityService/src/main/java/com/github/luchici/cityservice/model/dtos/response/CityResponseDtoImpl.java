package com.github.luchici.cityservice.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CityResponseDtoImpl implements CityResponseDto {
    private String cityName;
    private String country;
    private Set<AttractionResponseDto> attractions;

}
