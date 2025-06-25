package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final String name;

    public TestService(@Value("${my.name:Han}") String name) {
        this.name = name;
    }

    public String test() {
        return "Hello, " + name;
    }
}
