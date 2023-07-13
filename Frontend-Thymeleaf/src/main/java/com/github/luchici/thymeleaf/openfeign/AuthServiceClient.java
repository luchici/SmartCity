package com.github.luchici.thymeleaf.openfeign;

import com.github.luchici.thymeleaf.model.dto.LoginData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="auth",url = "http:// localhost:8082/api/auth")
public interface AuthServiceClient {

    @PostMapping( "/login")
    String login(@RequestBody LoginData loginData);

}
