package com.docflow.api.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TextractService {

    private final TextractClient textractClient;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String extractText(String s3Key) {
        S3Object s3Object = S3Object.builder()
                .bucket(bucketName)
                .name(s3Key)
                .build();

        Document document = Document.builder()
                .s3Object(s3Object)
                .build();

        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                .document(document)
                .build();

        DetectDocumentTextResponse response = textractClient.detectDocumentText(request);
        return response.blocks().stream()
                .filter(block -> block.blockType() == BlockType.LINE)
                .map(Block::text)
                .collect(Collectors.joining("\n"));
    }
}