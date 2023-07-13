package com.github.luchici.thymeleaf.controllers;

import com.github.luchici.thymeleaf.model.City;
import com.github.luchici.thymeleaf.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CityController {

    private CityService cityService;

    // TODO: Test the Endpoint
    @GetMapping("/cities/{cityName}")
    public String getCityView(Model model, @PathVariable String cityName) {
        City chosenCity = cityService.getCityByCityName(cityName);
        model.addAttribute("city", chosenCity);
        return "model/city";
    }
}
