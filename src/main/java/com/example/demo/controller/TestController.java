package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TestUserRequest;
import com.example.demo.dto.TestUserResponse;
import com.example.demo.entity.TestEntity;
import com.example.demo.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping
    public List<TestEntity> getAllTestEntities() {
        return testService.findAll();
    }

    @GetMapping("/{id}")
    public TestEntity getTestEntityById(@PathVariable String id) {
        return testService.findById(id)
                .orElseThrow(() -> new RuntimeException("엔티티를 찾을 수 없습니다: " + id));
    }

    @PostMapping
    public TestEntity createTestEntity(@RequestBody Map<String, Object> request) {
        String id = (String) request.get("id");
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) request.get("data");
        return testService.saveTestEntity(id, data);
    }

    @PutMapping("/{id}/data")
    public TestEntity addJsonData(
            @PathVariable String id,
            @RequestParam String key,
            @RequestBody Object value) {
        return testService.addJsonData(id, key, value);
    }

    @DeleteMapping("/{id}/data/{key}")
    public TestEntity removeJsonData(
            @PathVariable String id,
            @PathVariable String key) {
        return testService.removeJsonData(id, key);
    }

    @PostMapping("/search/contains")
    public List<TestEntity> searchByJsonDataContains(@RequestBody Map<String, Object> jsonData) {
        return testService.findByJsonDataContains(jsonData);
    }

    @GetMapping("/search/key/{key}")
    public List<TestEntity> searchByJsonDataContainsKey(@PathVariable String key) {
        return testService.findByJsonDataContainsKey(key);
    }

    @GetMapping("/search/key-value")
    public List<TestEntity> searchByJsonDataKeyValue(
            @RequestParam String key,
            @RequestParam String value) {
        return testService.findByJsonDataKeyValue(key, value);
    }

    @GetMapping("/search/key-value-int")
    public List<TestEntity> searchByJsonDataKeyValueInt(
            @RequestParam String key,
            @RequestParam Integer value) {
        return testService.findByJsonDataKeyValueInt(key, value);
    }

    @GetMapping("/search/key-value-gt")
    public List<TestEntity> searchByJsonDataKeyValueGreaterThan(
            @RequestParam String key,
            @RequestParam Integer value) {
        return testService.findByJsonDataKeyValueGreaterThan(key, value);
    }

    @GetMapping("/search/key-value-lt")
    public List<TestEntity> searchByJsonDataKeyValueLessThan(
            @RequestParam String key,
            @RequestParam Integer value) {
        return testService.findByJsonDataKeyValueLessThan(key, value);
    }

    @GetMapping("/search/key-value-like")
    public List<TestEntity> searchByJsonDataKeyValueLike(
            @RequestParam String key,
            @RequestParam String pattern) {
        return testService.findByJsonDataKeyValueLike(key, pattern);
    }

    @DeleteMapping("/{id}")
    public void deleteTestEntity(@PathVariable String id) {
        testService.deleteById(id);
    }

    @PostMapping("/sample-data")
    public String createSampleData(@RequestBody Map<String, Object> request) {
        // 샘플 데이터 1
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "홍길동");
        data1.put("age", 30);
        data1.put("city", "서울");
        data1.put("skills", new String[] { "Java", "Spring", "PostgreSQL" });
        testService.saveTestEntity("user1", data1);

        // 샘플 데이터 2
        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "김철수");
        data2.put("age", 25);
        data2.put("city", "부산");
        data2.put("skills", new String[] { "Python", "Django", "MySQL" });
        testService.saveTestEntity("user2", data2);

        // 샘플 데이터 3
        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "이영희");
        data3.put("age", 28);
        data3.put("city", "대구");
        data3.put("skills", new String[] { "JavaScript", "React", "MongoDB" });
        testService.saveTestEntity("user3", data3);

        return "샘플 데이터가 생성되었습니다.";
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
