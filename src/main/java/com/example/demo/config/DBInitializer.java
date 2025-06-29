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

            // GIN 인덱스 테스트를 위한 샘플 데이터 생성
            if (all.isEmpty()) {
                createSampleDataForGinTest();
                System.out.println("GIN 인덱스 테스트용 샘플 데이터 생성 완료");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createSampleDataForGinTest() {
        // 샘플 데이터 1
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "홍길동");
        data1.put("age", 30);
        data1.put("city", "서울");
        data1.put("skills", new String[] { "Java", "Spring", "PostgreSQL" });
        data1.put("department", "개발팀");
        data1.put("salary", 5000000);

        TestEntity entity1 = TestEntity.builder()
                .id("user1")
                .data(data1)
                .build();
        testRepository.save(entity1);

        // 샘플 데이터 2
        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "김철수");
        data2.put("age", 25);
        data2.put("city", "부산");
        data2.put("skills", new String[] { "Python", "Django", "MySQL" });
        data2.put("department", "백엔드팀");
        data2.put("salary", 4500000);

        TestEntity entity2 = TestEntity.builder()
                .id("user2")
                .data(data2)
                .build();
        testRepository.save(entity2);

        // 샘플 데이터 3
        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "이영희");
        data3.put("age", 28);
        data3.put("city", "대구");
        data3.put("skills", new String[] { "JavaScript", "React", "MongoDB" });
        data3.put("department", "프론트엔드팀");
        data3.put("salary", 4800000);

        TestEntity entity3 = TestEntity.builder()
                .id("user3")
                .data(data3)
                .build();
        testRepository.save(entity3);

        System.out.println("GIN 인덱스 테스트용 데이터 생성 완료:");
        System.out.println("- user1: 홍길동 (개발팀, 30세, 서울)");
        System.out.println("- user2: 김철수 (백엔드팀, 25세, 부산)");
        System.out.println("- user3: 이영희 (프론트엔드팀, 28세, 대구)");
    }
}
