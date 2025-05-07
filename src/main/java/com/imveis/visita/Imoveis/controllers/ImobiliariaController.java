
package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imobiliarias")
@RequiredArgsConstructor
public class ImobiliariaController {

    private final ImobiliariaRepository imobiliariaRepository;

    @PostMapping
    public ResponseEntity<Imobiliaria> cadastrar(@RequestBody Imobiliaria imobiliaria){
        return ResponseEntity.ok(imobiliariaRepository.save(imobiliaria));
    }

    @GetMapping
    public List<Imobiliaria> listar(){
        return imobiliariaRepository.findAll();
    }
}