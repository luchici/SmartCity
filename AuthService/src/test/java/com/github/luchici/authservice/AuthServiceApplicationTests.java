package com.github.luchici.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthServiceApplicationTests {
    @Test
    void contextLoads(WebApplicationContext context) {
        assertNotNull(context);
    }
}
