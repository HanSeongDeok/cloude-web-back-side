package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FileUploadRequest;
import com.example.demo.dto.FileUploadResponse;
import com.example.demo.entity.FileEntity;
import com.example.demo.repository.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileUploadResponse processFile(FileUploadRequest fileData) {
        // 파일 데이터 검증
        if (fileData.getFileName() == null || fileData.getFileName().trim().isEmpty()) {
            throw new IllegalArgumentException("파일명은 필수입니다.");
        }

        if (fileData.getFileContent() == null || fileData.getFileContent().trim().isEmpty()) {
            throw new IllegalArgumentException("파일 내용은 필수입니다.");
        }

        // 파일 ID 생성
        String fileId = UUID.randomUUID().toString();
        Long fileSize = 0L;
        try {
            fileSize = Long.parseLong(fileData.getFileSize().toString());
        } catch (NumberFormatException e) {
            fileSize = Long.parseLong(fileData.getFileSize().toString().trim().replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            fileSize = 0L;
        }

        // Entity 생성 및 저장
        FileEntity fileEntity = FileEntity.builder()
                .id(fileId)
                .fileName(fileData.getFileName())
                .fileContent(fileData.getFileContent())
                .fileType(fileData.getFileType())
                .fileSize(fileSize)
                .build();

        fileRepository.save(fileEntity);

        return FileUploadResponse.builder()
                .success(true)
                .message("파일이 성공적으로 업로드되었습니다.")
                .fileId(fileId)
                .downloadUrl("/api/files/" + fileId)
                .build();
    }

    public FileEntity getFileById(String fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
}