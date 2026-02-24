package com.docflow.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {
    private Long id;
    private String fileName;
    private String status;
    private String extractedText;
    private LocalDateTime uploadedAt;
}
