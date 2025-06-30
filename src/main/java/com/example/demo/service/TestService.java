package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TestUserRequest;
import com.example.demo.dto.TestUserResponse;
import com.example.demo.entity.TestEntity;
import com.example.demo.entity.TestUserEntity;
import com.example.demo.repository.TestRepository;
import com.example.demo.repository.TestUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestService {
    private final String name;
    private final TestUserRepository testUserRepository;
    private final TestRepository testRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TestService(@Value("${my.name:Han}") String name,
            TestUserRepository testUserRepository,
            TestRepository testRepository) {
        this.name = name;
        this.testUserRepository = testUserRepository;
        this.testRepository = testRepository;
    }

    public String test() {
        testRepository.save(TestEntity.builder()
                .id(name + UUID.randomUUID().toString())
                .data(Map.of("name", name, "age", 29, "email", "test@test.com"))
                .build());
        return "Hello, " + name;
    }

    public Object getAgeByGin() {
        String jsonCondition = "{\"age\": 29}";
        List<TestEntity> results = testRepository.findByDataContains(jsonCondition);
        return results;
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
        LocalDateTime date = testUserRepository.findFirstByCreatedAtIsNotNullOrderByCreatedAtDesc().getCreatedAt();
        return TestUserResponse.builder()
                .success(true)
                .message("Success")
                .createdAt(date)
                .build();
    }
}
