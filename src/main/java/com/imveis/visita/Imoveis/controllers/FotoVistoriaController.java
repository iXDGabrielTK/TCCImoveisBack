package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.service.FotoVistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fotosVistoria")
public class FotoVistoriaController {
    private final FotoVistoriaService fotoVistoriaService;

    @Autowired
    public FotoVistoriaController(FotoVistoriaService fotoVistoriaService) {
        this.fotoVistoriaService = fotoVistoriaService;
    }

    @GetMapping
    public List<FotoVistoria> getAllFotosVistoria() {
        return fotoVistoriaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<FotoVistoria> getFotoVistoriaById(@PathVariable BigInteger id) {
        return fotoVistoriaService.findById(id);
    }

    @PostMapping
    public FotoVistoria createFotoVistoria(@RequestBody FotoVistoria fotoVistoria) {
        return fotoVistoriaService.save(fotoVistoria);
    }

    @DeleteMapping("/{id}")
    public void deleteFotoVistoria(@PathVariable BigInteger id) {
        fotoVistoriaService.deleteById(id);
    }
}
