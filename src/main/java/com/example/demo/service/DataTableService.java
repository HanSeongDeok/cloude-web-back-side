package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * 데이터 테이블 전체 검색
     * 
     * @param limit  페이지 크기
     * @param offset 오프셋
     * @return 검색된 데이터 테이블 리스트
     */
    public List<DataTableEntity> searchDataTable(int limit, int offset) {
        return dataTableRepository.searchAllColumnsValue(limit, offset);
    }

    /**
     * 데이터 테이블 전체 검색 V2
     * 
     * @param limit  페이지 크기
     * @param offset 오프셋
     * @return 검색된 데이터 테이블 리스트
     */
    public List<DataTableEntity> searchDataTableV2(int limit, int offset) {
        return dataTableRepository.searchAllColumnsValueV2(limit, offset);
    }

    /**
     * 데이터 테이블 전체 검색 V3
     * 
     * @param limit  페이지 크기
     * @param offset 오프셋
     * @return 검색된 데이터 테이블 리스트
     */
    public List<DataTableEntity> searchDataTableV3(int limit, int offset) {
        Long lastId = dataTableRepository.searchDataTableLastId(offset);
        return dataTableRepository.searchAllColumnsValueV3(limit, lastId);
    }

    /**
     * 데이터 테이블 검색
     * 
     * @param search 검색어
     * @return 검색된 데이터 테이블 리스트
     */
    public List<DataTableEntity> searchDataTable(String search, int limit, int offset) {
        return dataTableRepository.searchCustomColumnsValueV2(search, limit, offset);
    }

    /**
     * 데이터 테이블 검색 (페이징 포함)
     * 
     * @param search 검색어
     * @param page   페이지 번호 (0부터 시작)
     * @param size   페이지 크기
     * @return 검색된 데이터 테이블 페이지
     */
    public Page<DataTableEntity> searchDataTablePageable(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return dataTableRepository.searchWithPgroongaPageable(search, pageable);
    }

    /**
     * 전체 데이터 개수 조회
     * 
     * @return 전체 데이터 개수
     */
    public long getTotalCount() {
        return dataTableRepository.count();
    }
}
