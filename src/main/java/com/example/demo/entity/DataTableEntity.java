package com.example.demo.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "data_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class DataTableEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tableNumber;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @Column
    private Long fileSize;

    @Column
    private String fileFormat;

    @Column
    private String fileModifiedDate;

    @Column
    private String fileCreator;

    @Column
    private String deliverableType;

    @Column
    private String testType;

    @Column
    private String vehicle;

    @Column
    private String driveType;

    @Column
    private String devStep;

    @Column
    private String ecu;

    @Column
    private String testItem;

    @Column
    private Boolean testResult;

    @Column
    private String tcNum;

    @Column
    private String swVer;

    @Column
    private String description;

    @Column
    private String memType;

    @Column
    private String depArr;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> customColumns;
}
