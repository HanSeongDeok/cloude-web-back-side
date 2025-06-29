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
    @Query(value = "SELECT * FROM test WHERE data @> :jsonData", nativeQuery = true)
    List<TestEntity> findByJsonDataContains(@Param("jsonData") String jsonData);

    // JSONB 필드에서 특정 키가 존재하는 엔티티 검색
    @Query(value = "SELECT * FROM test WHERE data ? :key", nativeQuery = true)
    List<TestEntity> findByJsonDataContainsKey(@Param("key") String key);

    // JSONB 필드에서 특정 키의 값으로 검색
    @Query(value = "SELECT * FROM test WHERE data->>:key = :value", nativeQuery = true)
    List<TestEntity> findByJsonDataKeyValue(@Param("key") String key, @Param("value") String value);

    // JSONB 필드에서 특정 키의 값이 특정 값과 일치하는 엔티티 검색 (숫자)
    @Query(value = "SELECT * FROM test WHERE (data->>:key)::int = :value", nativeQuery = true)
    List<TestEntity> findByJsonDataKeyValueInt(@Param("key") String key, @Param("value") Integer value);

    // JSONB 필드에서 특정 키의 값이 특정 값보다 큰 엔티티 검색 (숫자)
    @Query(value = "SELECT * FROM test WHERE (data->>:key)::int > :value", nativeQuery = true)
    List<TestEntity> findByJsonDataKeyValueGreaterThan(@Param("key") String key, @Param("value") Integer value);

    // JSONB 필드에서 특정 키의 값이 특정 값보다 작은 엔티티 검색 (숫자)
    @Query(value = "SELECT * FROM test WHERE (data->>:key)::int < :value", nativeQuery = true)
    List<TestEntity> findByJsonDataKeyValueLessThan(@Param("key") String key, @Param("value") Integer value);

    // JSONB 필드에서 특정 키의 값이 배열에 포함되는 엔티티 검색
    @Query(value = "SELECT * FROM test WHERE data->>:key ?| array[:values]", nativeQuery = true)
    List<TestEntity> findByJsonDataKeyValueInArray(@Param("key") String key, @Param("values") String[] values);

    // JSONB 필드에서 특정 키의 값이 문자열 패턴과 일치하는 엔티티 검색 (LIKE)
    @Query(value = "SELECT * FROM test WHERE data->>:key LIKE :pattern", nativeQuery = true)
    List<TestEntity> findByJsonDataKeyValueLike(@Param("key") String key, @Param("pattern") String pattern);
}