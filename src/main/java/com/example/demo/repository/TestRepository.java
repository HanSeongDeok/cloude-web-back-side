package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TestEntity;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, String> {
    // JSONB 필드에서 특정 키-값 쌍으로 검색 (GIN 인덱스 활용)
    @Query(value = "SELECT * FROM test WHERE data @> CAST(:jsonCondition AS jsonb)", nativeQuery = true)
    List<TestEntity> findByDataContains(@Param("jsonCondition") String jsonCondition);
}