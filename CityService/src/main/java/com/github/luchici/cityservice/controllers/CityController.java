package com.github.luchici.cityservice.controllers;

import com.github.luchici.cityservice.model.dtos.request.CityRequestDto;
import com.github.luchici.cityservice.model.dtos.response.CityResponseDto;
import com.github.luchici.cityservice.services.CityService;
import com.github.luchici.cityservice.util.CityConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
@Validated
public class CityController {
    private final CityService cityService;

    @GetMapping("/{cityName}")
    public ResponseEntity<CityResponseDto> getCityByName(@PathVariable @Pattern(regexp = "^[a-zA-Z -]+$") String cityName) {
        CityResponseDto city = cityService.getCityResponseDtoByCityName(cityName);
        return ResponseEntity.ok(city);
    }

    @GetMapping()
    public ResponseEntity<List<CityResponseDto>> getCities(@RequestParam(defaultValue = "1") @Min(1L) int page) {
        Page<CityResponseDto> cityDtoPage = cityService.getCities(page);
        return ResponseEntity
                .status(OK)
                .header("Total Pages", String.valueOf(cityDtoPage.getTotalPages()))
                .header("Total Elements", String.valueOf(cityDtoPage.getTotalElements()))
                .body(cityDtoPage.toList());
    }

    @PostMapping
    public ResponseEntity<CityResponseDto> addCity(@RequestBody CityRequestDto cityRequestDto) {
        cityService.addCity(cityRequestDto);
        return ResponseEntity
                .status(CREATED)
                .header(CONTENT_LOCATION, "/cities/" + cityRequestDto.getCityName())
                .body(cityConvertor.requestDtoToResponseDto(cityRequestDto));
    }

    @PutMapping("/{cityName}")
    public ResponseEntity<CityResponseDto> updateCity(
            @PathVariable @Pattern(regexp = "^[a-zA-Z -]+$") String cityName,
            @RequestBody @Valid CityRequestDto cityRequestDto) {
        boolean isNewCity = cityService.updateOrCreateCity(cityName, cityRequestDto);
        if (isNewCity) {
            return ResponseEntity.ok()
                    .header(CONTENT_LOCATION, "/cities/" + cityRequestDto.getCityName())
                    .body(cityConvertor.requestDtoToResponseDto(cityRequestDto));
        }
        return ResponseEntity.ok(cityConvertor.requestDtoToResponseDto(cityRequestDto));
    }

    @DeleteMapping("/{cityName}")
    public ResponseEntity<Void> deleteCity(@PathVariable @Pattern(regexp = "^[a-zA-Z -]+$") String cityName) {
        cityService.deleteByCityName(cityName);
        return ResponseEntity.noContent().build();
    }
}
