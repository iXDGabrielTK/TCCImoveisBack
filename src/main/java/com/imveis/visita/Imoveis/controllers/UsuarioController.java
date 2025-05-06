package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.service.AuthService;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import com.imveis.visita.Imoveis.service.VisitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    private final VisitanteService visitanteService;
    private final FuncionarioService funcionarioService;
    private final UsuarioService usuarioService;
    private final AuthService authService;


    @Autowired
    public UsuarioController(
            VisitanteService visitanteService,
            FuncionarioService funcionarioService,
            UsuarioService usuarioService, AuthService authService
    ) {
        this.visitanteService = visitanteService;
        this.funcionarioService = funcionarioService;
        this.usuarioService = usuarioService;
        this.authService = authService;
    }


    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuario instanceof Visitante visitante) {
                visitante.setDataCadastro(LocalDateTime.now());
                authService.encodePassword(visitante);
                visitante = visitanteService.save(visitante);
                authService.atribuirRoleParaUsuario(visitante, "VISITANTE");
                return ResponseEntity.ok(visitante);
            }

            if (usuario instanceof Funcionario funcionario) {
                authService.encodePassword(funcionario);
                funcionario = funcionarioService.save(funcionario);
                authService.atribuirRoleParaUsuario(funcionario, "FUNCIONARIO");
                return ResponseEntity.ok(funcionario);
            }

            return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable BigInteger id) {
        try {
            Optional<Usuario> usuario = usuarioService.findById(id);

            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable BigInteger id, @RequestBody Usuario usuarioAtualizado) {
        System.out.println("Requisição recebida para atualizar o usuário com ID: " + id);
        System.out.println("Dados recebidos: " + usuarioAtualizado);

        try {
            if (usuarioAtualizado.getNome() == null || usuarioAtualizado.getEmail() == null) {
                return ResponseEntity.badRequest().body("Campos obrigatórios ausentes: nome e login são obrigatórios.");
            }

            Optional<Usuario> usuarioExistente = usuarioService.findById(id);

            if (usuarioExistente.isPresent()) {
                Usuario usuario = usuarioExistente.get();

                if (usuarioAtualizado.getNome() != null) {
                    usuario.setNome(usuarioAtualizado.getNome());
                }
                if (usuarioAtualizado.getTelefone() != null) {
                    usuario.setTelefone(usuarioAtualizado.getTelefone());
                }
                if (usuarioAtualizado.getEmail() != null) {
                    usuario.setEmail(usuarioAtualizado.getEmail());
                }
                // Handle password separately to ensure it's encoded
                if (usuarioAtualizado.getSenha() != null) {
                    // Store the raw password temporarily
                    String rawPassword = usuarioAtualizado.getSenha();
                    // Clear it from the update object to prevent double encoding
                    usuarioAtualizado.setSenha(null);
                    // Set it on the user object after other properties are updated
                    usuario.setSenha(rawPassword);
                }

                Usuario usuarioSalvo = usuarioService.save(usuario);
                return ResponseEntity.ok(usuarioSalvo);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> listarUsuariosPorTipo(@PathVariable String tipo) {
        try {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
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
