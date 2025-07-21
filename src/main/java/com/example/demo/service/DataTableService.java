package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ColumnNamesEntity;
import com.example.demo.entity.DataTableEntity;
import com.example.demo.repository.ColumnNamesRepository;
import com.example.demo.repository.DataTableRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataTableService {
    private final DataTableRepository dataTableRepository;
    private final ColumnNamesRepository columnNamesRepository;

    /**
     * 데이터 테이블 컬럼명 반환 (동적 변경 대응)
     * 
     * @return 데이터 테이블 컬럼명 맵
     */
    public Map<String, String> getDataTableColumns() {
        Map<String, String> response = new HashMap<>();
        columnNamesRepository.findAll().stream()
                .map(ColumnNamesEntity::getColumnNames)
                .forEach(columnNames -> {
                    response.putAll(columnNames);
                });
        return response;
    }

    /**
     * 모든 데이터 테이블 조회
     * 
     * @return 모든 데이터 테이블 리스트
     */
    public List<DataTableEntity> getAllDataTable() {
        return dataTableRepository.findAll();
    }
}
