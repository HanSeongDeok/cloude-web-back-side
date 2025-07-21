package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DataTableService;

@RestController
@RequestMapping("/data-table")
@CrossOrigin(origins = "*")
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
}