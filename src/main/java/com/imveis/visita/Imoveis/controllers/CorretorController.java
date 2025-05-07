package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigInteger;
import java.util.Optional;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorRepository corretorRepository;

    @PostMapping
    public ResponseEntity<Corretor> createCorretor(@RequestBody Corretor corretor) {
        return ResponseEntity.ok(corretorRepository.save(corretor));
    }

    @GetMapping
    public List<Corretor> listTodos(){
        return corretorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable BigInteger id){
        Optional<Corretor> corretor = corretorRepository.findById(id);
        return corretor.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable BigInteger id){
        if(!corretorRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        corretorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}