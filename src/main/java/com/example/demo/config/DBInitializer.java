package com.example.demo.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.entity.ColumnNamesEntity;
import com.example.demo.entity.DataTableEntity;
import com.example.demo.entity.TestEntity;
import com.example.demo.repository.ColumnNamesRepository;
import com.example.demo.repository.DataTableRepository;
import com.example.demo.repository.TestRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 데이터베이스 초기화 작업 수행 핸들러
 */
@Component
@RequiredArgsConstructor
public class DBInitializer {
    final TestRepository testRepository;
    final DataTableRepository dataTableRepository;
    final ColumnNamesRepository columnNamesRepository;

    @PostConstruct
    public void testRepositoryInit() {
        try {
            List<TestEntity> all = testRepository.findAll();

            for (TestEntity entity : all) {
                Map<String, Object> data = entity.getData();
                if (data == null) {
                    data = new HashMap<>();
                }
                if (!data.containsKey("name")) {
                    data.put("name", "default");
                    entity.setData(data);
                }
            }

            testRepository.saveAll(all);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void dataTableInit() {
        try {
            // dataTableRepository.deleteAll();
            if (dataTableRepository.findAll().isEmpty()) {
                dataTableRepository.save(DataTableEntity.builder()
                        .tableNumber("1")
                        .fileName("test.csv")
                        .filePath("C:/test.csv")
                        .fileSize(100L)
                        .fileFormat("csv")
                        .fileModifiedDate("2021-01-01")
                        .fileCreator("test")
                        .deliverableType("test")
                        .testType("test")
                        .vehicle("test")
                        .driveType("test")
                        .devStep("test")
                        .ecu("test")
                        .testItem("test")
                        .testResult(true)
                        .tcNum("test")
                        .swVer("test")
                        .description("test")
                        .memType("test")
                        .depArr("test")
                        .customColumns(new HashMap<>())
                        .build());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void initColumnNames() {
        // @NOTE: 변경 하고 싶을 시 사용
        columnNamesRepository.deleteAll();
        try {
            if (columnNamesRepository.findAll().isEmpty()) {
                Map<String, String> columnNames = new LinkedHashMap<>();
                columnNames.put("fileName", "파일명");
                columnNames.put("filePath", "파일저장경로");
                columnNames.put("fileSize", "용량");
                columnNames.put("fileFormat", "형식");
                columnNames.put("fileModifiedDate", "파일생성일");
                columnNames.put("fileCreator", "등록자");
                columnNames.put("tableNumber", "등록번호");
                columnNames.put("deliverableType", "산출물분류");
                columnNames.put("testType", "시험분류");
                columnNames.put("vehicle", "차종");
                columnNames.put("driveType", "구동타입");
                columnNames.put("devStep", "개발단계");
                columnNames.put("ecu", "제어기");
                columnNames.put("testItem", "시험항목");
                columnNames.put("testResult", "시험결과");
                columnNames.put("tcNum", "TC#");
                columnNames.put("swVer", "소프트웨어버전");
                columnNames.put("description", "산출물설명");
                columnNames.put("memType", "메모리타입");
                columnNames.put("depArr", "출발지-도착지");
                columnNames.put("createdAt", "최초등록날짜");
                columnNames.put("updatedAt", "최종수정날짜");

                columnNamesRepository.save(ColumnNamesEntity.builder()
                        .columnNames(columnNames)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}