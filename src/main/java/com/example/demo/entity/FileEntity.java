package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(columnDefinition = "TEXT")
    private String fileContent;
    
    @Column
    private String fileType;
    
    @Column
    private Long fileSize;
    
    @Column
    private LocalDateTime uploadDate;
    
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
} 