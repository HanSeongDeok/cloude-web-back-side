package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DataTableEntity;

@Repository
public interface DataTableRepository extends JpaRepository<DataTableEntity, Long> {
}
