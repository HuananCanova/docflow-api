package com.docflow.api.service;


import com.docflow.api.dto.DocumentResponseDTO;
import com.docflow.api.entity.Document;
import com.docflow.api.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final S3Service s3Service;
    private final TextractService textractService;

    public DocumentResponseDTO upload(MultipartFile file) throws IOException {
        String s3key = s3Service.uploadFile(file);

        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setS3Key(s3key);
        Document saved = documentRepository.save(document);
        String extratecText = textractService.extractText(s3key);
        document.setExtractedText(extratecText);
        document.setStatus("COMPLETED");

        Document updated = documentRepository.save(document);

        return toDTO(updated);
    }

    public List<DocumentResponseDTO> findAll(){
        return documentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DocumentResponseDTO findById(Long id){
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return toDTO(document);
    }

    private DocumentResponseDTO toDTO(Document document){
        DocumentResponseDTO dto = new DocumentResponseDTO();
        dto.setId(document.getId());
        dto.setFileName(document.getFileName());
        dto.setStatus(document.getStatus());
        dto.setExtractedText(document.getExtractedText());
        dto.setUploadedAt(document.getUploadedAt());
        return dto;
    }



}
