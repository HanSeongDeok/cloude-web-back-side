package com.example.demo.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private boolean success;
    private String message;
    private String fileId;
    private String downloadUrl;
    // 필요한 다른 응답 필드들을 추가할 수 있습니다
} 