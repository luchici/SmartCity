package com.github.luchici.thymeleaf.auth;

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
    NewUser expectedNewUser = new NewUser();

    @Test
    void getSignUpPage() {
        String returnedTemplate = authController.getSignUpPage(new NewUser(), model);
        assertEquals("auth/signup", returnedTemplate);
    }

    @Test
    void signUpIsSuccessfully() {
        String returnTemplate = authController.signUpUser(expectedNewUser, model);
        assertEquals("redirect:/home", returnTemplate, "Wrong template returned");

    }

    @Test
    void signUpFails() {
        doThrow(new RuntimeException()).doNothing().when(authService).singUp(expectedNewUser);
        Throwable ex = assertThrows(RuntimeException.class, ()->authController.signUpUser(expectedNewUser,model));
    }


}
