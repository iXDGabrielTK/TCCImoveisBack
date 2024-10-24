package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuaController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/api/rota")
    public String getDados() {
        return "Dados do Backend";
    }

    // Endpoint para listar todos os usuários
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    // Endpoint para buscar um usuário específico por ID
    @GetMapping("/{id}")
    public Optional<Usuario> getUsuarioById(@PathVariable BigInteger id) {
        return usuarioService.findById(id);
    }

    // Endpoint para cadastrar um novo usuário
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    // Endpoint para deletar um usuário por ID
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable BigInteger id) {
        usuarioService.deleteById(id);
    }
}
