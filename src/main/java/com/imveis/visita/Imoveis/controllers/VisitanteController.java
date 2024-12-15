package com.imveis.visita.Imoveis.controllers;


import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.service.VisitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visitantes")
public class VisitanteController {

    private final VisitanteService visitanteService;

    @Autowired
    public VisitanteController(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
    }

    @GetMapping
    public List<Visitante> getAllVisitantes() {
        return visitanteService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criarVisitante(@RequestBody Visitante visitante) {
        try {
            Visitante novoVisitante = visitanteService.save(visitante);
            return ResponseEntity.ok(novoVisitante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar visitante: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Optional<Visitante> getVisitanteById(@PathVariable BigInteger id) {
        return visitanteService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVisitante(@PathVariable BigInteger id) {
        visitanteService.deleteById(id);
    }
}

