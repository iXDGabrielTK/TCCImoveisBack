package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.FotoImovelRepository;
import com.imveis.visita.Imoveis.service.FotoImovelService;
import com.imveis.visita.Imoveis.service.ImovelService;
import com.imveis.visita.Imoveis.infra.s3.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fotosImovel")
public class FotoImovelController {

    private final FotoImovelService fotoImovelService;
    private final ImovelService imovelService;
    private final FotoImovelRepository fotoImovelRepository;
    private final S3StorageService s3StorageService; // Injetado

    @Autowired
    public FotoImovelController(FotoImovelService fotoImovelService, ImovelService imovelService, FotoImovelRepository fotoImovelRepository, S3StorageService s3StorageService) {
        this.fotoImovelService = fotoImovelService;
        this.imovelService = imovelService;
        this.fotoImovelRepository = fotoImovelRepository;
        this.s3StorageService = s3StorageService; // Atribua
    }

    @GetMapping
    public List<FotoImovel> getAllFotosImovel() {
        return fotoImovelService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<FotoImovel> getFotoImovelById(@PathVariable Long id) {
        return fotoImovelService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createFotosImovel(@RequestBody List<String> urls, @RequestParam Long imovelId) {
        Imovel imovel = imovelService.findById(imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado"));

        List<FotoImovel> fotos = urls.stream()
                .map(String::trim)
                .filter(url -> !url.isEmpty() && url.startsWith("http"))
                .map(url -> {
                    FotoImovel foto = new FotoImovel();
                    foto.setImovel(imovel);
                    foto.setUrlFotoImovel(url);
                    return fotoImovelService.save(foto);
                })
                .collect(Collectors.toList());

        if (fotos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma URL válida fornecida.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fotos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFotoImovel(@PathVariable("id") Long idFotoImovel) {
        try {
            Optional<FotoImovel> fotoOptional = fotoImovelService.findById(idFotoImovel);
            if (fotoOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            FotoImovel foto = fotoOptional.get();

            // 1. Excluir o arquivo do S3
            s3StorageService.deleteFile(foto.getUrlFotoImovel());

            // 2. Excluir o registro do banco de dados
            fotoImovelRepository.deleteById(idFotoImovel);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public List<String> uploadAndGetUrls(List<MultipartFile> files, Long imovelId) {
        List<String> uploadedUrls = new java.util.ArrayList<>();
        try {
            Imovel imovel = imovelService.findById(imovelId)
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado para upload de fotos."));

            // *** Mude esta linha: ***
            String folderPath = "imoveis-fotos/" + imovel.getIdImovel(); // <-- AQUI! Use "imoveis-fotos/"

            for (MultipartFile file : files) {
                String url = s3StorageService.uploadFile(file, folderPath);
                FotoImovel foto = FotoImovel.builder().imovel(imovel).urlFotoImovel(url).build();
                fotoImovelService.save(foto);
                uploadedUrls.add(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao fazer upload das fotos do imóvel para S3", e);
        }
        return uploadedUrls;
    }
}