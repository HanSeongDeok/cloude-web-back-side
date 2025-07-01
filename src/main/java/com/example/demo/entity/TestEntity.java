package com.example.demo.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class TestEntity extends BaseTimeEntity {
    @Id
    private String id;

    @Column
    private String fileName;

    @Column
    private String parentId;

    @Column
    private String fileType;

    @Column
    private boolean isFolder;

    @Column
    private Long size;

    @Column
    private String filePath;

    // JSONB 형태로 저장될 필드 (GIN 인덱스 적용)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> data;
}
