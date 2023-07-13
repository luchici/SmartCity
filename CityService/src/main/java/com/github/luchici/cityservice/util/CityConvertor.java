package com.github.luchici.cityservice.util;

import com.github.luchici.cityservice.model.dtos.request.CityRequestDto;
import com.github.luchici.cityservice.model.dtos.response.CityResponseDto;
import com.github.luchici.cityservice.model.dtos.response.CityResponseDtoImpl;
import com.github.luchici.cityservice.model.entities.Attraction;
import com.github.luchici.cityservice.model.entities.City;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class CityConvertor extends AbstractConverter<City,CityRequestDto> {

    private final ModelMapper modelMapper;
    private final AttractionConvertor attractionConvertor;

    public CityRequestDto cityToRequestDto(City city) {
        return modelMapper.typeMap(City.class, CityRequestDto.class)
                .addMappings(mapper -> {
                    mapper.map(source -> source.getCityName(), CityRequestDto::setCityName);
                    mapper.map(source -> source.getCountry(), CityRequestDto::setCountry);
                    mapper.map(source -> source.getAttractions(), (CityRequestDto cityRequestDto, Set<Attraction> attractions) ->
                            attractions.stream()
                                    .map(attractionConvertor::attractionToRequestDto)
                                    .collect(toSet()));
                })
                .map(city);
        // return modelMapper.map(city, CityRequestDto.class);
    }

    public City cityRequestDtoToCity(CityRequestDto cityRequestDto) {
        return modelMapper.map(cityRequestDto, City.class);
    }

    public CityResponseDto cityToResponseDto(City city) {
        // modelMapper.typeMap(City.class, CityResponseDtoImpl.class)
        //         .addMappings(
        //                 mapper -> mapper.map())
        return modelMapper.map(city, CityResponseDtoImpl.class);
    }

    public CityResponseDto requestDtoToResponseDto(CityRequestDto cityRequestDto) {
        return modelMapper.map(cityRequestDto, CityResponseDtoImpl.class);
    }

    @Override
    protected CityRequestDto convert(City source) {
        return null;
    }
}
