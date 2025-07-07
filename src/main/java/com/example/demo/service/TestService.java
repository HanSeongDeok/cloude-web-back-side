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
import com.example.demo.repository.TestFileRepository;
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
    private final TestFileRepository testFileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TestService(@Value("${my.name:Han}") String name,
            TestUserRepository testUserRepository,
            TestRepository testRepository,
            TestFileRepository testFileRepository) {
        this.name = name;
        this.testUserRepository = testUserRepository;
        this.testRepository = testRepository;
        this.testFileRepository = testFileRepository;
    }

    public String test() {
        testRepository.save(TestEntity.builder()
                .id(name + UUID.randomUUID().toString())
                .isFolder(false)
                .data(Map.of("name", name, "age", 30, "email", "test2@test.com"))
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

    public TestService saveTestFolder() {
        String folderId = name + UUID.randomUUID().toString();

        testRepository.save(TestEntity.builder()
                .id(folderId)
                .folderName("test_01")
                .isFolder(true)
                .folderPath("Localhost/test_01")
                .data(Map.of("name", "Han Seong Deok", "type", "folder"))
                .build());
        // 하위 파일 1
        testRepository.save(TestEntity.builder()
                .id("file_" + UUID.randomUUID())
                .isFolder(false)
                .filePath("Localhost/test_01/readme.txt")
                .fileName("readme.txt")
                .fileType("txt")
                .parentFolder(folderId)
                .size(1024L)
                .data(Map.of("description", "sample text file", "author", "admin"))
                .build());
        // 하위 파일 2
        testRepository.save(TestEntity.builder()
                .id("file_" + UUID.randomUUID())
                .isFolder(false)
                .filePath("Localhost/test_01/image.png")
                .fileName("image.png")
                .fileType("png")
                .parentFolder(folderId)
                .size(204800L)
                .data(Map.of("resolution", "1024x768", "author", "admin"))
                .build());
        return this;
    }

    public TestUserResponse getTestResp() {
        LocalDateTime date = testRepository.findFirstByCreatedAtIsNotNullOrderByCreatedAtDesc().getCreatedAt();
        return TestUserResponse.builder()
                .success(true)
                .message("Success")
                .createdAt(date)
                .build();
    }

    public TestUserResponse getTestUserResp() {
        LocalDateTime date = testUserRepository.findFirstByCreatedAtIsNotNullOrderByCreatedAtDesc().getCreatedAt();
        return TestUserResponse.builder()
                .success(true)
                .message("Success")
                .createdAt(date)
                .build();
    }

    // TestEntity 삭제 메서드
    public void deleteTestEntity(String id) {
        testRepository.deleteById(id);
        log.info("TestEntity deleted with id: {}", id);
    }

    // TestEntity 업데이트 메서드
    public TestEntity updateTestEntity(String id, Map<String, Object> newData) {
        TestEntity existingEntity = testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestEntity not found with id: " + id));

        existingEntity.setData(newData);
        TestEntity updatedEntity = testRepository.save(existingEntity);
        log.info("TestEntity updated with id: {}", id);
        return updatedEntity;
    }

    // 모든 TestEntity 삭제 메서드
    public void deleteAllTestEntities() {
        testRepository.deleteAll();
        log.info("All TestEntities deleted");
    }

    // 조건에 맞는 TestEntity 삭제 메서드
    public void deleteTestEntitiesByCondition(String jsonCondition) {
        List<TestEntity> entitiesToDelete = testRepository.findByDataContains(jsonCondition);
        testRepository.deleteAll(entitiesToDelete);
        log.info("Deleted {} TestEntities matching condition: {}", entitiesToDelete.size(), jsonCondition);
    }
}
