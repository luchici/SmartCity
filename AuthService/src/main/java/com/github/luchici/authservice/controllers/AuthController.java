package com.github.luchici.authservice.controllers;

import com.github.luchici.authservice.model.dtos.NewAccountDto;
import com.github.luchici.authservice.model.dtos.ResponseAccountDto;
import com.github.luchici.authservice.model.embedded.LoginData;
import com.github.luchici.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid final LoginData loginData) {
        System.out.println("======================================================");
        System.out.println(this.getClass().getSimpleName());
        System.out.println("here");
        System.out.println("======================================================");
        authService.login(loginData);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseAccountDto> register(@RequestBody @Valid final NewAccountDto newAccountDto) {
        authService.register(newAccountDto);
        final ResponseAccountDto responseAccountDto = modelMapper.map(newAccountDto, ResponseAccountDto.class);
        return ResponseEntity
                .status(CREATED)
                .header(CONTENT_LOCATION, "/api/auth/user/" + newAccountDto.getUsername())
                .body(responseAccountDto);
    }
}
