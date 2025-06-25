package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {
    private String fileName;
    private String fileContent;
    private String fileType;
    private Object fileSize;
    // 필요한 다른 필드들을 추가할 수 있습니다
}