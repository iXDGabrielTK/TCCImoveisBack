// src/main/java/com/imveis/visita/Imoveis/controllers/FuncionarioController.java
package com.imveis.visita.Imoveis.controllers;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final UsuarioService usuarioService;

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService, UsuarioService usuarioService) {
        this.funcionarioService = funcionarioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Funcionario> getAllFuncionarios() {
        return funcionarioService.findAll();
    }

    @PostMapping
    public Funcionario save(@RequestBody Funcionario funcionario) {
        boolean senhaFoiInformada = funcionario.getSenha() != null && !funcionario.getSenha().trim().isEmpty();
        Usuario salvo = usuarioService.save(funcionario, senhaFoiInformada);
        return (Funcionario) salvo;
    }

    @GetMapping("/{id}")
    public Optional<Funcionario> getFuncionarioById(@PathVariable Long id) {
        return funcionarioService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFuncionario(@PathVariable Long id) {
        funcionarioService.deleteById(id);
    }
}
