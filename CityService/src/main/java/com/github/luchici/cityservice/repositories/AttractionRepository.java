package com.github.luchici.cityservice.repositories;

import com.github.luchici.cityservice.model.dtos.response.AttractionResponseDto;
import com.github.luchici.cityservice.model.entities.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Integer> {

    // Page<AttractionDto> findAllAttractionDtoBy(Pageable pageable);

    // @Query(value = "SELECT NEW com.github.luchici.cityservice.model.dtos.AttractionDto(a.name) from Attraction AS a")
    @Transactional(readOnly = true)
    Page<AttractionResponseDto> findAllAttractionReturnedDtoBy(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<AttractionResponseDto> findAttractionDtoByNameIgnoreCase(String attractionName);

    Optional<Attraction> findByNameIgnoreCase(String attractionName);

    void deleteByNameIgnoreCase(String attractionName);
}
