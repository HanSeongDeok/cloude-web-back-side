package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TestFileEntity;

@Repository
public interface TestFileRepository extends JpaRepository<TestFileEntity, Long> {

    // JSON 데이터 조건으로 검색 (GIN 인덱스 활용)
    @Query(value = "SELECT * FROM test_file WHERE data::text LIKE %:jsonCondition%", nativeQuery = true)
    List<TestFileEntity> findByDataContains(@Param("jsonCondition") String jsonCondition);

    TestFileEntity findFirstByCreatedAtIsNotNullOrderByCreatedAtDesc();
}