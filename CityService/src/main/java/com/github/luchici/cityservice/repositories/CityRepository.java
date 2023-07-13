package com.github.luchici.cityservice.repositories;

import com.github.luchici.cityservice.model.dtos.response.CityResponseDto;
import com.github.luchici.cityservice.model.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    @Transactional(readOnly = true)
    Page<CityResponseDto> findAllCityDtoBy(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<CityResponseDto> findCityDtoByCityNameIgnoreCase(String cityName);

    Optional<City> findByCityNameIgnoreCase(String cityName);

    void deleteBycityNameIgnoreCase(String cityName);
}
