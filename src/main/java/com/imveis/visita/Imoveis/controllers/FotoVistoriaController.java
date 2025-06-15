package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.entities.Vistoria;
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

    @Autowired
    public FotoVistoriaController(FotoVistoriaService fotoVistoriaService, VistoriaService vistoriaService) {
        this.fotoVistoriaService = fotoVistoriaService;
        this.vistoriaService = vistoriaService;
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
    public ResponseEntity<Object> createFotosVistoria(@RequestBody String urls, @RequestParam Long vistoriaId) {
        Vistoria vistoria = vistoriaService.findById(vistoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Vistoria não encontrada"));

        List<FotoVistoria> fotos = Arrays.stream(urls.split(","))
                .map(String::trim)
                .filter(url -> !url.isEmpty() && url.startsWith("http"))
                .map(url -> {
                    FotoVistoria foto = new FotoVistoria();
                    foto.setVistoria(vistoria);
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
    public void deleteFotoVistoria(@PathVariable Long id) {
        fotoVistoriaService.deleteById(id);
    }
}
