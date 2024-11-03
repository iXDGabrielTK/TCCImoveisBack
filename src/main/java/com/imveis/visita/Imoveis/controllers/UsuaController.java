package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.LoginRequest;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.UsuaRepository;
import com.imveis.visita.Imoveis.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuaController {

    @Autowired
    private UsuaRepository usuaRepository;

    private final UsuarioService usuarioService;

    @Autowired
    public UsuaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/api/rota")
    public String getDados() {
        return "Dados do Backend";
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getUsuarioById(@PathVariable BigInteger id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuaRepository.findByLoginAndSenha(loginRequest.getLogin(), loginRequest.getSenha());
        if (usuario != null) {
            return ResponseEntity.ok("Login bem-sucedido!"); // Em um caso real, retornaria um token aqui
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable BigInteger id) {
        usuarioService.deleteById(id);
    }
}
