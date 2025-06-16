package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.AmbienteVistoria;
import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.infra.s3.S3Service;
import com.imveis.visita.Imoveis.service.AmbienteVistoriaService;
import com.imveis.visita.Imoveis.service.FotoVistoriaService;
import com.imveis.visita.Imoveis.service.VistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fotosVistoria")
public class FotoVistoriaController {

    private final FotoVistoriaService fotoVistoriaService;
    private final VistoriaService vistoriaService;
    private final AmbienteVistoriaService ambienteVistoriaService;
    private final S3Service s3Service; // Injete o S3Service


    @Autowired
    public FotoVistoriaController(FotoVistoriaService fotoVistoriaService, VistoriaService vistoriaService, AmbienteVistoriaService ambienteVistoriaService, S3Service s3Service) {
        this.fotoVistoriaService = fotoVistoriaService;
        this.vistoriaService = vistoriaService;
        this.ambienteVistoriaService = ambienteVistoriaService;
        this.s3Service = s3Service;
    }

    @GetMapping
    public List<FotoVistoria> getAllFotosVistoria() {
        return fotoVistoriaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<FotoVistoria> getFotoVistoriaById(@PathVariable Long id) {
        return fotoVistoriaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createFotosVistoria(@RequestBody String urls, @RequestParam Long ambienteId) {
        AmbienteVistoria ambiente = ambienteVistoriaService.findById(ambienteId)
                .orElseThrow(() -> new IllegalArgumentException("Ambiente da vistoria não encontrado"));

        List<FotoVistoria> fotos = Arrays.stream(urls.split(","))
                .map(String::trim)
                .filter(url -> !url.isEmpty() && url.startsWith("http"))
                .map(url -> {
                    FotoVistoria foto = new FotoVistoria();
                    foto.setAmbiente(ambiente); // ✅ associando corretamente
                    foto.setUrlFotoVistoria(url);
                    return fotoVistoriaService.save(foto);
                })
                .collect(Collectors.toList());

        if (fotos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma URL válida fornecida.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fotos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFotoVistoria(@PathVariable Long id) {
        try {
            FotoVistoria foto = fotoVistoriaService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Foto da vistoria não encontrada com o ID: " + id));

            // Extrair o nome do arquivo da URL para exclusão no S3
            // Ex: "https://seu-bucket.s3.amazonaws.com/prefixo/nome_do_arquivo.jpg"
            // Você precisa extrair "prefixo/nome_do_arquivo.jpg"
            String urlCompleta = foto.getUrlFotoVistoria();
            String bucketName = s3Service.getBucketName(); // Você precisaria de um getter para o bucket no S3Service
            String fileNameInS3 = urlCompleta.substring(urlCompleta.indexOf(".s3.amazonaws.com/") + ".s3.amazonaws.com/".length());

            s3Service.deleteFile(fileNameInS3); // Remove o arquivo do S3
            fotoVistoriaService.deleteById(id); // Remove o registro do banco de dados

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 se não encontrar
        } catch (RuntimeException e) {
            // Erro ao excluir do S3
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 se erro no S3
        }
    }
}
