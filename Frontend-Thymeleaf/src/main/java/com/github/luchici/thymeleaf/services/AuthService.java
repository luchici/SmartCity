package com.github.luchici.thymeleaf.services;

import com.github.luchici.thymeleaf.model.dto.LoginData;
import com.github.luchici.thymeleaf.model.dto.NewAccountDto;
import com.github.luchici.thymeleaf.openfeign.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthServiceClient auth;

    public void login(LoginData loginData) {

        System.out.println("======================================================");
        System.out.println(this.getClass().getSimpleName());
        System.out.println(auth.login(loginData));
        System.out.println("======================================================");

        // TODO: fail to login throw an exception
    }

    public void singUp(NewAccountDto newAccountDto) {
        // TODO: fail to signUp throw an exception
    }
}
