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

            // 기존 데이터 확인
            List<TestEntity> all = testRepository.findAll();
            System.out.println("기존 엔티티 수: " + all.size());

            // 기존 데이터에 name 필드가 없으면 추가
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
            System.out.println("기존 데이터에 'name: default' 초기화 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
