package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ColumnNamesEntity;

@Repository
public interface ColumnNamesRepository extends JpaRepository<ColumnNamesEntity, Long> {
}
