package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
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
                .data(Map.of("name", name, "age", 29))
                .build());
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

    /**
     * JSONB 데이터를 포함하는 엔티티 검색 (GIN 인덱스 활용)
     */
    public List<TestEntity> findByJsonDataContains(Map<String, Object> jsonData) {
        try {
            String jsonString = objectMapper.writeValueAsString(jsonData);
            return testRepository.findByJsonDataContains(jsonString);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 중 오류 발생", e);
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    /**
     * 특정 키가 존재하는 엔티티 검색
     */
    public List<TestEntity> findByJsonDataContainsKey(String key) {
        return testRepository.findByJsonDataContainsKey(key);
    }

    /**
     * 특정 키-값 쌍으로 엔티티 검색
     */
    public List<TestEntity> findByJsonDataKeyValue(String key, String value) {
        return testRepository.findByJsonDataKeyValue(key, value);
    }

    /**
     * 특정 키의 숫자 값으로 엔티티 검색
     */
    public List<TestEntity> findByJsonDataKeyValueInt(String key, Integer value) {
        return testRepository.findByJsonDataKeyValueInt(key, value);
    }

    /**
     * 특정 키의 값이 특정 값보다 큰 엔티티 검색
     */
    public List<TestEntity> findByJsonDataKeyValueGreaterThan(String key, Integer value) {
        return testRepository.findByJsonDataKeyValueGreaterThan(key, value);
    }

    /**
     * 특정 키의 값이 특정 값보다 작은 엔티티 검색
     */
    public List<TestEntity> findByJsonDataKeyValueLessThan(String key, Integer value) {
        return testRepository.findByJsonDataKeyValueLessThan(key, value);
    }

    /**
     * 특정 키의 값이 배열에 포함되는 엔티티 검색
     */
    public List<TestEntity> findByJsonDataKeyValueInArray(String key, String[] values) {
        return testRepository.findByJsonDataKeyValueInArray(key, values);
    }

    /**
     * 특정 키의 값이 문자열 패턴과 일치하는 엔티티 검색
     */
    public List<TestEntity> findByJsonDataKeyValueLike(String key, String pattern) {
        return testRepository.findByJsonDataKeyValueLike(key, pattern);
    }

    /**
     * 엔티티 생성 또는 업데이트 (JSONB 데이터 포함)
     */
    public TestEntity saveTestEntity(String id, Map<String, Object> data) {
        TestEntity entity = TestEntity.builder()
                .id(id)
                .data(data != null ? data : new HashMap<>())
                .build();

        return testRepository.save(entity);
    }

    /**
     * 엔티티의 JSONB 데이터에 키-값 추가
     */
    public TestEntity addJsonData(String id, String key, Object value) {
        Optional<TestEntity> optionalEntity = testRepository.findById(id);
        if (optionalEntity.isPresent()) {
            TestEntity entity = optionalEntity.get();
            Map<String, Object> data = entity.getData();
            if (data == null) {
                data = new HashMap<>();
            }
            data.put(key, value);
            entity.setData(data);
            return testRepository.save(entity);
        }
        throw new RuntimeException("엔티티를 찾을 수 없습니다: " + id);
    }

    /**
     * 엔티티의 JSONB 데이터에서 특정 키 제거
     */
    public TestEntity removeJsonData(String id, String key) {
        Optional<TestEntity> optionalEntity = testRepository.findById(id);
        if (optionalEntity.isPresent()) {
            TestEntity entity = optionalEntity.get();
            Map<String, Object> data = entity.getData();
            if (data != null) {
                data.remove(key);
                entity.setData(data);
                return testRepository.save(entity);
            }
        }
        throw new RuntimeException("엔티티를 찾을 수 없습니다: " + id);
    }

    /**
     * 모든 엔티티 조회
     */
    @Transactional(readOnly = true)
    public List<TestEntity> findAll() {
        return testRepository.findAll();
    }

    /**
     * ID로 엔티티 조회
     */
    @Transactional(readOnly = true)
    public Optional<TestEntity> findById(String id) {
        return testRepository.findById(id);
    }

    /**
     * 엔티티 삭제
     */
    public void deleteById(String id) {
        testRepository.deleteById(id);
    }
}
