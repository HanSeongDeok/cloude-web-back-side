package com.example.demo.entity;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String folderName;

    @Column
    private String folderPath;

    @Column
    private Boolean isFolder;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @Column
    private String fileType;

    @Column
    private Long size;

    @Column
    private String parentFolder;

    // 폴더에 포함된 파일들과의 관계
    @OneToMany(mappedBy = "parentFolder", fetch = FetchType.LAZY)
    private List<TestFileEntity> files;

    // JSONB 형태로 저장될 필드 (GIN 인덱스 적용)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> data;
}
