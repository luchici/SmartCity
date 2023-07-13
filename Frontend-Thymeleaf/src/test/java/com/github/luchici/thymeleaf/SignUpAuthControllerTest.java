package com.github.luchici.thymeleaf;

import com.github.luchici.thymeleaf.controllers.AuthController;
import com.github.luchici.thymeleaf.model.dto.NewAccountDto;
import com.github.luchici.thymeleaf.services.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

class SignUpAuthControllerTest {

    private final AuthService authService = Mockito.mock(AuthService.class);
    private AuthController authController = new AuthController(authService);
    Model model = new ConcurrentModel();
    NewAccountDto expectedNewAccountDto = new NewAccountDto();

    @Test
    void getSignUpPage() {
        String returnedTemplate = authController.getSignUpPage(new NewAccountDto(), model);
        assertEquals("auth/signup", returnedTemplate);
    }

    @Test
    void signUpIsSuccessfully() {
        String returnTemplate = authController.signUpUser(expectedNewAccountDto, model);
        assertEquals("redirect:/home", returnTemplate, "Wrong template returned");

    }

    @Test
    void signUpFails() {
        doThrow(new RuntimeException()).doNothing().when(authService).singUp(expectedNewAccountDto);
        Throwable ex = assertThrows(RuntimeException.class, ()->authController.signUpUser(expectedNewAccountDto,model));
    }


}
