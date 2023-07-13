package com.github.luchici.thymeleaf;

import com.github.luchici.thymeleaf.services.CityService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

class CityControllerTest {
    public CityService cityService = new CityService();
    // @Test
    void getCityView(Model model, @PathVariable String cityName) {
        // City chosenCity = cityService.getCityByCityName(cityName);
        // model.addAttribute("city", chosenCity);
    }

    // @Test
    void givenCity_WillBeInTheModel() {
        // Given
        // City expectedCity = new City("Madrid", "Spain");
        // Mockito.when(homeService.getCity("Madrid")).thenReturn(expectedCity);
        // // When
        // homeController.getCityView(model, "Madrid");
        // City actualCity = (City) model.getAttribute("city");
        // // Then
        // assertEquals(actualCity,expectedCity);
    }
}
