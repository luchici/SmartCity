package com.github.luchici.cityservice.services;

import com.github.luchici.cityservice.exception.NotFoundException;
import com.github.luchici.cityservice.model.dtos.request.CityRequestDto;
import com.github.luchici.cityservice.model.dtos.response.CityResponseDto;
import com.github.luchici.cityservice.model.entities.City;
import com.github.luchici.cityservice.model.entities.ImageData;
import com.github.luchici.cityservice.repositories.CityRepository;
import com.github.luchici.cityservice.util.CityConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CityService {
    public static final int PAGE_SIZE_DEFAULT = 5;
    private final CityRepository cityRepository;
    private final CityConvertor cityConvertor = null;

    public Page<CityResponseDto> getCities(int page) {
        page = page - 1;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE_DEFAULT);
        Page<CityResponseDto> cities = cityRepository.findAllCityDtoBy(pageable);
        if (page > cities.getTotalPages()) {
            pageable = PageRequest.of(cities.getTotalPages(), PAGE_SIZE_DEFAULT);
            cities = cityRepository.findAllCityDtoBy(pageable);
        }
        return cities;
    }

    // TODO
    // public CityCardDto getAllCityCardsDto(int page) {
    //     return null;
    // }

    public CityResponseDto getCityResponseDtoByCityName(String cityName) {
        return cityRepository.findCityDtoByCityNameIgnoreCase(cityName)
                .orElseThrow(() -> new NotFoundException("There is no city with the name of " + cityName));
    }

    public City getCityByCityName(String cityName) {
        City city = cityRepository.findByCityNameIgnoreCase(cityName)
                .orElseThrow(() -> new NotFoundException("There is no city with the name of " + cityName));
        return city;
    }

    @Transactional(readOnly = false)
    public void addCity(CityRequestDto newCityRequest) {
        // Optional<City> oldCity = cityRepository.findByCityNameIgnoreCase(newCityDto.getCityName());
        // if (oldCity.isEmpty()) {
        City newCity = cityConvertor.cityRequestDtoToCity(newCityRequest);
        cityRepository.save(newCity);
        // }
        // throw new CityAlredyExisted("City was found in the database.");
    }

    @Transactional(readOnly = false)
    public boolean updateOrCreateCity(String cityName, CityRequestDto newCityRequestDto) {
        // TODO: Check if the imgaes from the dto are in the image from the neity
        // TODO: Check if the attraction from the dto are in the image from the neity
        Optional<City> oldCity = cityRepository.findByCityNameIgnoreCase(cityName);
        if (oldCity.isEmpty()) {
            City newCity = cityConvertor.cityRequestDtoToCity(newCityRequestDto);
            City savedCity = cityRepository.save(newCity);
            return true;
        }
        updateExitingCity(newCityRequestDto, oldCity.get());
        return false;
    }

    @Transactional(readOnly = false)
    public void deleteByCityName(String cityName) {
        cityRepository.deleteBycityNameIgnoreCase(cityName);
    }

    public CityResponseDto addImageToCity(String cityName, MultipartFile file) {
        log.info("Filename : " + file);
        log.info("Size : " + file.getSize());
        log.info("Content Type : " + file.getContentType());
        ImageData image = new ImageData(file);
        City updateCity = getCityByCityName(cityName);
        updateCity.getImages().add(image);
        return cityConvertor.cityToResponseDto(updateCity);
    }

    private void updateExitingCity(CityRequestDto newCityRequestDto, City oldCity) {
        oldCity.setCityName(newCityRequestDto.getCityName());
        oldCity.setCountry(newCityRequestDto.getCountry());
    }
}
