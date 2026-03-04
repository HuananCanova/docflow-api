package com.docflow.api.service;


import com.docflow.api.exception.UnsupportedFileTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    private static final List<String> SUPPORTED_TYPES = List.of(
            "image/jpeg", "image/png", "image/tiff", "application/pdf"

    );


    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {

        if(!SUPPORTED_TYPES.contains((file.getContentType()))) {
            throw new UnsupportedFileTypeException(
                    "File type not supported: " + file.getContentType() +
                            ". Supported types: JPEG, PNG, TIFF, PDF"
            );
        }

        String S3Key = "documents/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(S3Key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return S3Key;
    }


}
