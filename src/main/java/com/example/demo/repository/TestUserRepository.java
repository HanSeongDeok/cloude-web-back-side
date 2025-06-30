package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TestUserEntity;

@Repository
public interface TestUserRepository extends JpaRepository<TestUserEntity, String> {
    TestUserEntity findFirstByCreatedAtIsNotNullOrderByCreatedAtDesc();
}
