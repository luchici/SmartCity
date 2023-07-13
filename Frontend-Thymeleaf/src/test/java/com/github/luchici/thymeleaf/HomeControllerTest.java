package com.github.luchici.thymeleaf;

import com.github.luchici.thymeleaf.controllers.HomeController;
import com.github.luchici.thymeleaf.home.HomeService;
import com.github.luchici.thymeleaf.model.City;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HomeControllerTest {
    private final Model model = new ConcurrentModel();
    @Autowired
    private HomeController homeController;
    @MockBean
    private HomeService homeService;
    private HttpSession session = new MockHttpSession();

    @Test
    void givenCityList_willBeInModel() {
        // Given
        List<City> expectedCities = List.of(
                new City("Craiova", "Romania"),
                new City("Warsaw", "Poland"),
                new City("Dublin", "Ireland"));
        Mockito.when(homeService.getCities()).thenReturn(expectedCities);
        // When
        // TODO: Tes the defautl setting for home page
        homeController.getHomeView(model,Optional.empty(),session);
        List<City> actualCities = (List<City>) session.getAttribute("cities");
        // Then
        assertEquals(expectedCities, actualCities);
    }


}
