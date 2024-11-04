// src/main/java/com/imveis/visita/Imoveis/controllers/FuncionarioController.java
package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public List<Funcionario> getAllFuncionarios() {
        return funcionarioService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criarFuncionario(@RequestBody Funcionario funcionario) {
        try {
            Funcionario novoFuncionario = funcionarioService.save(funcionario);
            return ResponseEntity.ok(novoFuncionario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar funcionário: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Optional<Funcionario> getFuncionarioById(@PathVariable BigInteger id) {
        return funcionarioService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFuncionario(@PathVariable BigInteger id) {
        funcionarioService.deleteById(id);
    }
}
