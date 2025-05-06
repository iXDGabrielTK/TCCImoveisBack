// src/main/java/com/imveis/visita/Imoveis/controllers/FuncionarioController.java
package com.imveis.visita.Imoveis.controllers;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FuncionarioService funcionarioService;

    //private Usuario usuario;

    @GetMapping
    public List<Funcionario> getAllFuncionarios() {
        return funcionarioService.findAll();
    }

    public Funcionario save(Funcionario funcionario) {

        Usuario salvo = usuarioService.save(funcionario);

        return (Funcionario) salvo;
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
