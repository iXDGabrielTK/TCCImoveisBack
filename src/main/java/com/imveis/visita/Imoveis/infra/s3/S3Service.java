package com.imveis.visita.Imoveis.infra.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String getBucketName() {
        return this.bucket;
    }

    public String uploadFile(String prefixo, MultipartFile file) {
        try {
            String fileName = prefixo + UUID.randomUUID() + "_" + file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return "https://" + bucket + ".s3.amazonaws.com/" + fileName;
        } catch (Exception e) {
            logger.error("Erro detalhado ao fazer upload para o S3: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao fazer upload para o S3", e);
        }
    }
    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            logger.info("Arquivo {} excluído com sucesso do S3.", fileName);
        } catch (Exception e) {
            logger.error("Erro ao excluir arquivo {} do S3: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Erro ao excluir arquivo do S3", e);
        }
    }
}