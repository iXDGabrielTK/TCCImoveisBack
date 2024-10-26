package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.service.FotoImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fotosImovel")
public class FotoImovelController {

    private final FotoImovelService fotoImovelService;

    @Autowired
    public FotoImovelController(FotoImovelService fotoImovelService) {
        this.fotoImovelService = fotoImovelService;
    }

    // Listar todas as fotos de imóveis
    @GetMapping
    public List<FotoImovel> getAllFotosImovel() {
        return fotoImovelService.findAll();
    }

    // Buscar uma foto de imóvel por ID
    @GetMapping("/{id}")
    public Optional<FotoImovel> getFotoImovelById(@PathVariable BigInteger id) {
        return fotoImovelService.findById(id);
    }

    // Cadastrar uma nova foto de imóvel
    @PostMapping
    public FotoImovel createFotoImovel(@RequestBody FotoImovel fotoImovel) {
        return fotoImovelService.save(fotoImovel);
    }

    // Deletar uma foto de imóvel por ID
    @DeleteMapping("/{id}")
    public void deleteFotoImovel(@PathVariable BigInteger id) {
        fotoImovelService.deleteById(id);
    }
}
