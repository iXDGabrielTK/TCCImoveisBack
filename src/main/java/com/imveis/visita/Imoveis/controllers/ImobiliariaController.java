
package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImobiliariaRequest;
import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imobiliarias")
@RequiredArgsConstructor
public class ImobiliariaController {

    private final ImobiliariaRepository imobiliariaRepository;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody ImobiliariaRequest request) {
        try {


            Imobiliaria imobiliaria = new Imobiliaria();
            imobiliaria.setNome(request.getNome());
            imobiliaria.setRazaoSocial(request.getRazaoSocial());
            imobiliaria.setCnpj(request.getCnpj());
            imobiliaria.setEmail(request.getEmail());
            imobiliaria.setCep(request.getCep());

            return ResponseEntity.ok(imobiliariaRepository.save(imobiliaria));

        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erro de integridade: CNPJ já cadastrado ou dados inválidos.");
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar imobiliária:" + e.getMessage());
        }
    }

    @GetMapping
    public List<Imobiliaria> listar(){
        return imobiliariaRepository.findAll();
    }
}