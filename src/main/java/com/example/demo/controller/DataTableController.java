package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DataTableEntity;
import com.example.demo.service.DataTableService;

@RestController
@RequestMapping("/api/data-table")
public class DataTableController {

    private final DataTableService dataTableService;

    @Autowired
    public DataTableController(DataTableService dataTableService) {
        this.dataTableService = dataTableService;
    }

    @GetMapping("/columns")
    public ResponseEntity<Map<String, String>> getDataTable() {
        return ResponseEntity.ok(dataTableService.getDataTableColumns());
    }

    @GetMapping("/all-data")
    public ResponseEntity<List<DataTableEntity>> getAllDataTable(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(dataTableService.searchDataTable(limit, offset));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DataTableEntity>> searchDataTable(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(dataTableService.searchDataTable(keyword, limit, offset));
    }

    @GetMapping("/all-data-v2")
    public ResponseEntity<List<DataTableEntity>> getAllDataTableV2(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(dataTableService.searchDataTableV2(limit, offset));
    }

    @GetMapping("/all-data-v3")
    public ResponseEntity<List<DataTableEntity>> getAllDataTableV3(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(dataTableService.searchDataTableV3(limit, offset));
    }

    @GetMapping("/total-count")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(dataTableService.getTotalCount());
    }

    @GetMapping("/search-pageable")
    public ResponseEntity<Page<DataTableEntity>> searchDataTablePageable(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(dataTableService.searchDataTablePageable(keyword, page, size));
    }
}