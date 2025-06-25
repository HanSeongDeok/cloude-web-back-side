package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.FileUploadRequest;
import com.example.demo.dto.FileUploadResponse;
import com.example.demo.service.FileService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestBody FileUploadRequest fileData) {
        try {
            FileUploadResponse response = fileService.processFile(fileData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(FileUploadResponse.builder()
                            .success(false)
                            .message("파일 처리 중 오류가 발생했습니다: " + e.getMessage())
                            .build());
        }
    }
}