package com.example.demo.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {
    private String fileName;
    private String fileContent;
    private String fileType;
    private Long fileSize;
    // 필요한 다른 필드들을 추가할 수 있습니다
} 