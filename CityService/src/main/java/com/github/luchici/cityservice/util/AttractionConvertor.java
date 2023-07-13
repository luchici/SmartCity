package com.github.luchici.cityservice.util;

import com.github.luchici.cityservice.model.dtos.request.AttractionRequestDto;
import com.github.luchici.cityservice.model.dtos.response.AttractionResponseDto;
import com.github.luchici.cityservice.model.dtos.response.AttractionResponseDtoImpl;
import com.github.luchici.cityservice.model.entities.Attraction;
import com.github.luchici.cityservice.model.entities.City;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttractionConvertor {
    private final ModelMapper modelMapper;

    public Attraction requestToAttraction(AttractionRequestDto attractionRequestDto, City city) {
        var attraction = modelMapper.map(attractionRequestDto, Attraction.class);
        attraction.setCity(city);
        return attraction;
    }

    public Attraction responseToAttraction(AttractionResponseDto attractionResponseDto) {
        return modelMapper.map(attractionResponseDto, Attraction.class);
    }

    public AttractionRequestDto attractionToRequestDto(Attraction attraction) {
        return modelMapper.map(attraction, AttractionRequestDto.class);
    }

    public AttractionResponseDto attractionToResponseDto(Attraction attraction) {
        return modelMapper.map(attraction, AttractionResponseDtoImpl.class);
    }

    public AttractionResponseDto requestDtoToResponseDto(AttractionRequestDto attractionRequestDto) {
        return modelMapper.map(attractionRequestDto, AttractionResponseDtoImpl.class);
    }
}
