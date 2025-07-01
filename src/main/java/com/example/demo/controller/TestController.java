package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TestUserRequest;
import com.example.demo.dto.TestUserResponse;
import com.example.demo.entity.TestEntity;
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

    @DeleteMapping("/test-delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteTestEntity(@PathVariable String id) {
        try {
            testService.deleteTestEntity(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "TestEntity deleted successfully",
                    "deletedId", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Fail: " + e.getMessage(),
                            "deletedId", id));
        }
    }

    @PutMapping("/test-update/{id}")
    public ResponseEntity<Object> updateTestEntity(
            @PathVariable String id,
            @RequestBody Map<String, Object> newData) {
        try {
            TestEntity updatedEntity = testService.updateTestEntity(id, newData);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "TestEntity updated successfully",
                    "updatedEntity", updatedEntity));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Fail: " + e.getMessage(),
                            "entityId", id));
        }
    }

    @DeleteMapping("/test-delete-all")
    public ResponseEntity<Map<String, Object>> deleteAllTestEntities() {
        try {
            testService.deleteAllTestEntities();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "All TestEntities deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Fail: " + e.getMessage()));
        }
    }
}
