package com.github.luchici.thymeleaf.controllers;

import com.github.luchici.thymeleaf.services.AuthService;
import com.github.luchici.thymeleaf.model.dto.LoginData;
import com.github.luchici.thymeleaf.model.dto.NewAccountDto;
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
    public String getLoginPage(@ModelAttribute("loginUser") LoginData loginData, Model model) {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String getSignUpPage(@ModelAttribute("newUser") NewAccountDto newAccountDto, Model model) {
        return "auth/signup";
    }

    @PostMapping("/login")
    public String loginUser(LoginData loginData, Model model) {
        authService.login(loginData);
        model.addAttribute("username", loginData.username);
        return "redirect:/home";
    }

    @PostMapping("/signup")
    public String signUpUser(NewAccountDto newAccountDto, Model model) {
        authService.singUp(newAccountDto);
        model.addAttribute("username", newAccountDto.username);
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
