package com.example.demo.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.demo.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test_file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class TestFileEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column
    private String fileName;

    @Column
    private String fileType;

    @Column
    private Long size;

    @Column
    private String filePath;

    @Column
    private Boolean isFolder;

    // 폴더와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private TestEntity parentFolder;

    // JSONB 형태로 저장될 필드 (GIN 인덱스 적용)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> data;
}