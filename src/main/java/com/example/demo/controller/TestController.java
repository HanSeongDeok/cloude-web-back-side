package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TestUserRequest;
import com.example.demo.dto.TestUserResponse;
import com.example.demo.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/test")
    public ResponseEntity<String> getTestUser() {
        return ResponseEntity.ok(testService.test());
    }

    @GetMapping("/test-gin")
    public ResponseEntity<Object> getAgeByGin() {
        return ResponseEntity.ok(testService.getAgeByGin());
    }

    @PostMapping("/test-user")
    public ResponseEntity<TestUserResponse> testUser(@RequestBody TestUserRequest request) {
        try {
            return ResponseEntity.ok(testService
                    .saveTestUser(request)
                    .getTestUser());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(TestUserResponse.builder()
                            .success(false)
                            .message("Fail: " + e.getMessage())
                            .build());
        }
    }
}
