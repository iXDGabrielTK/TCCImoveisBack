package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.infra.s3.S3Service; // Seu serviço S3 existente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Se precisar de autorização
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/image-proxy")
public class ImageProxyController {

    private final S3Client s3Client;
    private final S3Service s3Service; // Para pegar o nome do bucket

    @Autowired
    public ImageProxyController(S3Client s3Client, S3Service s3Service) {
        this.s3Client = s3Client;
        this.s3Service = s3Service;
    }

    // Este endpoint irá atuar como um proxy para as imagens do S3.
    // Ele recebe a URL do S3 como um parâmetro de query e retorna a imagem.
    // A anotação @PreAuthorize("hasRole('FUNCIONARIO')") é opcional, adicione se apenas funcionários puderem gerar relatórios
    @PreAuthorize("hasRole('FUNCIONARIO')") // Exemplo de autorização, ajuste conforme sua regra de segurança
    @GetMapping(value = "/**") // Usamos /** para capturar qualquer caminho após /image-proxy
    public ResponseEntity<byte[]> proxyImage(@RequestParam String s3Url) {
        try {
            // Decodifica a URL para lidar com caracteres especiais
            String decodedS3Url = URLDecoder.decode(s3Url, StandardCharsets.UTF_8);

            // Extrai o nome do bucket e a chave (caminho do arquivo) da URL completa do S3
            // Ex: "https://meu-bucket-fotos-vistoria.s3.sa-east-1.amazonaws.com/imoveis-fotos/1/foto.png"
            // Queremos: bucketName = "meu-bucket-fotos-vistoria" e key = "imoveis-fotos/1/foto.png"
            String bucketName = s3Service.getBucketName(); // Seu S3Service já tem o nome do bucket

            // Encontra a parte da chave após ".com/"
            int comIndex = decodedS3Url.indexOf(".com/");
            if (comIndex == -1) {
                throw new IllegalArgumentException("URL S3 inválida: não contém '.com/'");
            }
            String key = decodedS3Url.substring(comIndex + ".com/".length());

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
                byte[] imageBytes = s3Object.readAllBytes();
                String contentType = s3Object.response().contentType();
                if (contentType == null) {
                    // Tente inferir o tipo de conteúdo se o S3 não fornecer
                    if (key.endsWith(".png")) contentType = MediaType.IMAGE_PNG_VALUE;
                    else if (key.endsWith(".jpeg") || key.endsWith(".jpg")) contentType = MediaType.IMAGE_JPEG_VALUE;
                    else if (key.endsWith(".gif")) contentType = MediaType.IMAGE_GIF_VALUE;
                    else contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Fallback
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(imageBytes);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de URL ou chave S3: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (IOException e) {
            System.err.println("Erro de I/O ao ler imagem do S3: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao proxy imagem do S3: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null); // Indica que o proxy não conseguiu acessar o S3
        }
    }
}