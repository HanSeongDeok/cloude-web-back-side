package com.example.demo.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.entity.TestEntity;
import com.example.demo.repository.TestRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DBInitializer {
    final TestRepository testRepository;

    @PostConstruct
    public void testRepositoryInit() {
        try {
            System.out.println("DBInitializer 실행됨");
            List<TestEntity> all = testRepository.findAll();

            for (TestEntity entity : all) {
                Map<String, Object> data = entity.getData();
                if (data == null) {
                    data = new HashMap<>();
                }
                if (!data.containsKey("name")) {
                    data.put("name", "default");
                    entity.setData(data);
                }
            }

            testRepository.saveAll(all);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
