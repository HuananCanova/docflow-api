package com.docflow.api.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String s3key;
    @Column(nullable = false)
    private String status;
    @Column(columnDefinition = "TEXT")
    private String extractedText;
    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist(){
        this.uploadedAt = LocalDateTime.now();
        this.status = "PROCESSING";
    }

}
