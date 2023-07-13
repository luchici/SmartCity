package com.github.luchici.cityservice.controllers;

import com.github.luchici.cityservice.model.dtos.request.AttractionRequestDto;
import com.github.luchici.cityservice.model.dtos.response.AttractionResponseDto;
import com.github.luchici.cityservice.services.AttractionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/attractions")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;
    private final ModelMapper modelMapper;

    @GetMapping("/{cityName}")
    public ResponseEntity<Set<AttractionResponseDto>> getAttractionByCityAndName(
            @PathVariable @Pattern(regexp = "^[a-zA-Z -]+$") String cityName,
            @RequestParam Optional<String> attractionName) {
        Set<AttractionResponseDto> attraction =
                attractionService.getAttractionDtoByCityAndName(cityName, attractionName);
        return ResponseEntity.ok(attraction);
    }

    @GetMapping()
    public ResponseEntity<Set<AttractionResponseDto>> getAttractions(@RequestParam(defaultValue = "1") @Min(1L) int page) {
        Page<AttractionResponseDto> attractionPage = attractionService.getAttractions(page);
        return ResponseEntity
                .status(OK)
                .header("Total Pages", String.valueOf(attractionPage.getTotalPages()))
                .header("Total Elements", String.valueOf(attractionPage.getTotalElements()))
                .body(attractionPage.toSet());
    }

    @PostMapping("/{cityName}")
    public ResponseEntity<Set<AttractionResponseDto>> addAttraction(
            @RequestBody Set<AttractionRequestDto> requestAttractions,
            @PathVariable String cityName) {
        attractionService.addAttraction(requestAttractions, cityName);
        Set<AttractionResponseDto> responseAttractions = requestAttractions.stream()
                .map(attractionConvertor::requestDtoToResponseDto)
                .collect(toSet());
        return ResponseEntity
                .status(CREATED)
                .header(CONTENT_LOCATION, "/attraction/" + cityName)
                .body(responseAttractions);
    }

    @PutMapping("/{attractionName}")
    public ResponseEntity<AttractionResponseDto> updateAttraction(
            @PathVariable @Pattern(regexp = "^[a-zA-Z -]+$") String attractionName,
            @RequestBody @Valid AttractionRequestDto attractionRequestDto) {
        boolean isNewAttraction = attractionService.updateOrCreateAttraction(attractionName, attractionRequestDto);

        if (isNewAttraction) {
            return ResponseEntity.ok()
                    .header(CONTENT_LOCATION, "/cities/" + attractionRequestDto.getName())
                    .body(attractionConvertor.requestDtoToResponseDto(attractionRequestDto));
        }
        return ResponseEntity.ok(attractionConvertor.requestDtoToResponseDto(attractionRequestDto));
    }

    @DeleteMapping("/{attractionName}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable @Pattern(regexp = "^[a-zA-Z -]+$") String attractionName) {
        attractionService.deleteByName(attractionName);
        return ResponseEntity.noContent().build();
    }
}
