package com.github.luchici.thymeleaf.auth;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SimpleSessionStatus;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class LogInAuthControllerTest {

    private AuthService authService= mock(AuthService.class);
    private AuthController authController = new AuthController(authService);
    private Model model = new ConcurrentModel();
    private LoginUser expectedLoginUser = new LoginUser("Test Username", "Test Password");

    private HttpSession session = new MockHttpSession();

    @Test
    void getLoginPage() {
        String returnedTemplate = authController.getLoginPage(new LoginUser(), model);
        assertEquals("auth/login", returnedTemplate);
    }

    @Test
    void loginIsSuccessfully() {
        // TODO: Test if username attribute is in the session
        String returnTemplate = authController.loginUser(expectedLoginUser,model);
        assertEquals("redirect:/home", returnTemplate, "Wrong template returned");
        String actualUsername = (String) model.getAttribute("username");
        assertEquals(expectedLoginUser.username,actualUsername);

    }

    @Test
    void loginFails() {
        // TODO: Test if username attribute is in the session
        // TODO: Test more specific about the exception type an message
        doThrow(new RuntimeException()).doNothing().when(authService).login(expectedLoginUser);
        Throwable ex = assertThrows(RuntimeException.class, ()->authController.loginUser(expectedLoginUser,model));
    }

    @Test
    void displayProfileWithALoggedAccount() {
        String returnTemplate = authController.displayProfile(new User());
        assertEquals("auth/profile", returnTemplate, "Wrong template returned");
    }

    @Test
    void logoutALoggedAccount() {
        SimpleSessionStatus status = new SimpleSessionStatus();
        String returnTemplate = authController.logout(status);
        assertEquals("home", returnTemplate, "Wrong template returned");
    }
}
