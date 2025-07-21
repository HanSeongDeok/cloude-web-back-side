package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ColumnNamesEntity;
import com.example.demo.repository.ColumnNamesRepository;
import com.example.demo.repository.DataTableRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataTableService {
    private final DataTableRepository dataTableRepository;
    private final ColumnNamesRepository columnNamesRepository;

    @Autowired
    public DataTableService(
            DataTableRepository dataTableRepository,
            ColumnNamesRepository columnNamesRepository) {
        this.dataTableRepository = dataTableRepository;
        this.columnNamesRepository = columnNamesRepository;
    }

    /**
     * 데이터 테이블 컬럼 반환
     * 
     * @return 데이터 테이블 컬럼 맵
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
}
