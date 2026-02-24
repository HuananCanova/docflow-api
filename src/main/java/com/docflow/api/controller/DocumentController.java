package com.docflow.api.controller;


import com.docflow.api.dto.DocumentResponseDTO;
import com.docflow.api.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponseDTO> upload(@RequestParam("file") MultipartFile file) throws IOException {
        DocumentResponseDTO response = documentService.upload(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> findAll(){
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(documentService.findById(id));
    }



}
