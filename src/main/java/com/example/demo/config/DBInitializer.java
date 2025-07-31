package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.demo.entity.DataTableEntity;
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
            // List<TestEntity> all = testRepository.findAll();

            // for (TestEntity entity : all) {
            // Map<String, Object> data = entity.getData();
            // if (data == null) {
            // data = new HashMap<>();
            // }
            // if (!data.containsKey("name")) {
            // data.put("name", "default");
            // entity.setData(data);
            // }
            // }

            // testRepository.saveAll(all);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void dataTableInit() {
        try {
            // @NOTE: 변경 하고 싶을 시 사용 주석 해제 사용 후 닫기!
            // dataTableRepository.deleteAll();

            // 10만개 데이터를 배치로 나누어 생성
            // if (dataTableRepository.findAll().isEmpty()) {
            // try {
            // generateBulkRandomDataInBatches(10000000, 1000); // 1000만개, 배치 크기 1000
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789가나다라마바사아자차카타파하아자차카타파하";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /**
     * TEST용 배치 Init Insert 입니다!!
     */
    private void generateBulkRandomDataInBatches(int totalCount, int batchSize) {
        Random random = new Random();
        int totalBatches = (int) Math.ceil((double) totalCount / batchSize);

        for (int batch = 0; batch < totalBatches; batch++) {
            List<DataTableEntity> batchEntities = new ArrayList<>();
            int startIndex = batch * batchSize + 1;
            int endIndex = Math.min((batch + 1) * batchSize, totalCount);

            for (int i = startIndex; i <= endIndex; i++) {
                Map<String, String> customColumns = new HashMap<>();
                customColumns.put("customField_1", "커스텀필드_" + i + "_" + generateRandomString(10));
                customColumns.put("customField_2", "테스트필드_" + i + "_" + generateRandomString(20));
                customColumns.put("customField_3", "테스트필드_" + i + "_" + generateRandomString(30));

                DataTableEntity entity = DataTableEntity.builder()
                        .tableNumber("DT-" + String.format("%06d", i))
                        .fileName("test.csv_" + i)
                        .filePath("C:/test.csv_" + i)
                        .fileSize(random.nextLong(1000000))
                        .fileFormat("csv")
                        .fileModifiedDate("2021-01-01")
                        .fileCreator("test_" + i)
                        .deliverableType("test")
                        .testType("test")
                        .vehicle("test")
                        .driveType("test")
                        .devStep("test")
                        .ecu("test")
                        .testItem("test")
                        .testResult(true)
                        .tcNum("TC-" + String.format("%06d", i))
                        .swVer("test")
                        .description("테스트 데이터 " + i + "번째 항목 - " + random.nextLong(1000000))
                        .memType("test")
                        .depArr("test")
                        .customColumns(customColumns)
                        .build();

                batchEntities.add(entity);
            }
            dataTableRepository.saveAll(batchEntities);
            batchEntities.clear();
        }
    }

    @PostConstruct
    public void initColumnNames() {
        // @NOTE: 변경 하고 싶을 시 사용 주석 해제 사용 후 닫기!
        // columnNamesRepository.deleteAll();
        try {
            // if (columnNamesRepository.findAll().isEmpty()) {
            // Map<String, String> columnNames = new LinkedHashMap<>();
            // columnNames.put("fileName", "파일명");
            // columnNames.put("filePath", "파일저장경로");
            // columnNames.put("fileSize", "용량");
            // columnNames.put("fileFormat", "형식");
            // columnNames.put("fileModifiedDate", "파일최종수정일");
            // columnNames.put("fileCreator", "등록자");
            // columnNames.put("tableNumber", "등록번호");
            // columnNames.put("deliverableType", "산출물분류");
            // columnNames.put("testType", "시험분류");
            // columnNames.put("vehicle", "차종");
            // columnNames.put("driveType", "구동타입");
            // columnNames.put("devStep", "개발단계");
            // columnNames.put("ecu", "제어기");
            // columnNames.put("testItem", "시험항목");
            // columnNames.put("testResult", "시험결과");
            // columnNames.put("tcNum", "TC#");
            // columnNames.put("swVer", "소프트웨어버전");
            // columnNames.put("description", "산출물설명");
            // columnNames.put("memType", "메모리타입");
            // columnNames.put("depArr", "출발지-도착지");
            // columnNames.put("createdAt", "최초등록날짜");
            // columnNames.put("updatedAt", "최종수정날짜");
            // columnNames.put("customField_1", "커스텀컬럼_1");
            // columnNames.put("customField_2", "커스텀컬럼_2");
            // columnNames.put("customField_3", "커스텀컬럼_3");

            // columnNamesRepository.save(ColumnNamesEntity.builder()
            // .columnNames(columnNames)
            // .build());
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}