package com.example.demo.repository;

import com.example.demo.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
    // 필요한 쿼리 메서드들을 추가할 수 있습니다
} 