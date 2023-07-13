package com.github.luchici.thymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SmartcityThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartcityThymeleafApplication.class, args);
	}
}
