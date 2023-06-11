package com.github.luchici.thymeleaf.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"username"})
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("loginUser") LoginUser loginUser, Model model) {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String getSignUpPage(@ModelAttribute("newUser") NewUser newUser, Model model) {
        return "auth/signup";
    }

    @PostMapping("/login")
    public String loginUser(LoginUser loginUser, Model model) {
        authService.login(loginUser);
        model.addAttribute("username", loginUser.username);
        return "redirect:/home";
    }

    @PostMapping("/signup")
    public String signUpUser(NewUser newUser, Model model) {
        authService.singUp(newUser);
        model.addAttribute("username", newUser.username);
        return "redirect:/home";
    }

    @GetMapping("/profile")
    public String displayProfile(User user) {
        return "auth/profile";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status){
        status.setComplete();
        return "home";
    }
}
