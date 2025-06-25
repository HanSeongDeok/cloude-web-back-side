package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {
    @Id
    private String id;
}
