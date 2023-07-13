package com.github.luchici.thymeleaf.controllers;

import com.github.luchici.thymeleaf.home.HomeService;
import com.github.luchici.thymeleaf.model.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping(value = {"/home/cities/pages/{pageNumber}", "/home","/"})
    public String getHomeView(Model model, @PathVariable Optional<Integer> pageNumber, HttpSession session) {
        // Extract total pages from response header
        // Use pageNumber as a parameter to call homeService
        List<City> cities = homeService.getCities();
        session.setAttribute("cities", cities);
        session.setAttribute("totalPages",cities.size());
        return "home";
    }



}
