package com.github.luchici.cityservice.exception;

public class CityAlredyExisted extends RuntimeException{
    public CityAlredyExisted(String message) {
        super(message);
    }
}
