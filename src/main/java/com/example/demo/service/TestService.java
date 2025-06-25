package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TestUserRequest;
import com.example.demo.dto.TestUserResponse;
import com.example.demo.entity.TestUserEntity;
import com.example.demo.repository.TestUserRepository;

@Service
public class TestService {
    private final String name;
    private final TestUserRepository testUserRepository;

    @Autowired
    public TestService(@Value("${my.name:Han}") String name, TestUserRepository testUserRepository) {
        this.name = name;
        this.testUserRepository = testUserRepository;
    }

    public String test() {
        return "Hello, " + name;
    }

    public TestService saveTestUser(TestUserRequest request) {
        TestUserEntity entity = TestUserEntity.builder()
                .id(request.getId())
                .name(request.getName())
                .age(request.getAge())
                .email(request.getEmail())
                .build();

        testUserRepository.save(entity);
        return this;
    }

    public TestUserResponse getTestUser() {
        return TestUserResponse.builder()
                .success(true)
                .message("Success")
                .build();
    }
}
