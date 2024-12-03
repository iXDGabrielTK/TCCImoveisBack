package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import com.imveis.visita.Imoveis.service.VisitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final VisitanteService visitanteService;
    private final FuncionarioService funcionarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, VisitanteService visitanteService, FuncionarioService funcionarioService) {
        this.usuarioService = usuarioService;
        this.visitanteService = visitanteService;
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuario instanceof Visitante visitante) {
                Visitante novoVisitante = visitanteService.save(visitante);
                return ResponseEntity.ok(novoVisitante);
            } else if (usuario instanceof Funcionario funcionario) {
                Funcionario novoFuncionario = funcionarioService.save(funcionario);
                return ResponseEntity.ok(novoFuncionario);
            } else {
                return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable BigInteger id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> listarUsuariosPorTipo(@PathVariable String tipo) {
        List<Usuario> usuarios;
        if ("visitante".equalsIgnoreCase(tipo)) {
            usuarios = usuarioService.findByTipo(Visitante.class);
        } else if ("funcionario".equalsIgnoreCase(tipo)) {
            usuarios = usuarioService.findByTipo(Funcionario.class);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable BigInteger id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok("Usuário excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir usuário: " + e.getMessage());
        }
    }
}
