package com.github.luchici.cityservice.services;

import com.github.luchici.cityservice.model.dtos.request.AttractionRequestDto;
import com.github.luchici.cityservice.model.dtos.response.AttractionResponseDto;
import com.github.luchici.cityservice.model.entities.Attraction;
import com.github.luchici.cityservice.model.entities.City;
import com.github.luchici.cityservice.repositories.AttractionRepository;
import com.github.luchici.cityservice.util.AttractionConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionRepository attractionRepository;
    private final CityService cityService;
    private final AttractionConvertor attractionConvertor;
    public static final int PAGE_SIZE_DEFAULT = 5;

    public Set<AttractionResponseDto> getAttractionDtoByCityAndName(String cityName, Optional<String> attractionName) {
        City city = cityService.getCityByCityName(cityName);
        Stream<Attraction> attractionStream = city.getAttractions().stream();
        if (attractionName.isPresent()) {
            attractionStream = city.getAttractions().stream()
                    .filter(att -> att.getName().contains(attractionName.get()));
        }
        return attractionStream
                .map(attractionConvertor::attractionToResponseDto)
                .collect(toSet());
    }

    public Page<AttractionResponseDto> getAttractions(int page) {
        page = page - 1;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE_DEFAULT);
        return attractionRepository.findAllAttractionReturnedDtoBy(pageable);
    }

    @Transactional
    public void addAttraction(Set<AttractionRequestDto> attractionRequestDto, String cityName) {
        final City city = cityService.getCityByCityName(cityName);
        Set<Attraction> attractions = attractionRequestDto.stream()
                .map(att -> attractionConvertor.requestToAttraction(att, city))
                .collect(toSet());
        city.setAttractions(attractions);
    }

    public void deleteByName(String attractionName) {
    }

    public boolean updateOrCreateAttraction(String attractionName, AttractionRequestDto attractionRequestDto) {
        return false;
    }
}
