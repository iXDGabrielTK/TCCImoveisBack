package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.UsuarioRequestDTO;
import com.imveis.visita.Imoveis.dtos.UsuarioResponseDTO;
import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.service.AuthService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import com.imveis.visita.Imoveis.service.VisitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final VisitanteService visitanteService;
    private final UsuarioService usuarioService;
    private final AuthService authService;

    @Autowired
    public UsuarioController(
            VisitanteService visitanteService,
            UsuarioService usuarioService, AuthService authService
    ) {
        this.visitanteService = visitanteService;
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioRequestDTO dto) {
        try {
            String tipo = dto.getTipo();

            if ("visitante".equalsIgnoreCase(tipo)) {
                Visitante visitante = new Visitante();
                visitante.setNome(dto.getNome());
                visitante.setEmail(dto.getEmail());
                visitante.setTelefone(dto.getTelefone());
                visitante.setSenha(dto.getSenha());
                visitante.setDataCadastro(LocalDateTime.now());
                authService.encodePassword(visitante);
                visitante = visitanteService.save(visitante);
                authService.atribuirRoleParaUsuario(visitante, "VISITANTE");
                UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(
                        visitante.getId(),
                        visitante.getNome(),
                        visitante.getEmail(),
                        visitante.getTelefone(),
                        "visitante",
                        null,
                        null
                );
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                UsuarioResponseDTO dto = new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getTipo(),
                        (usuario instanceof Funcionario f) ? f.getCpf() : null,
                        (usuario instanceof Corretor c) ? c.getCreci() : null
                );
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }

            Usuario usuario = usuarioOpt.get();

            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setTelefone(dto.getTelefone());

            boolean senhaFoiAlterada = dto.getSenha() != null && !dto.getSenha().trim().isEmpty();

            if (senhaFoiAlterada) {
                usuario.setSenha(dto.getSenha());
            }

            Usuario atualizado = usuarioService.save(usuario, senhaFoiAlterada);

            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(
                    atualizado.getId(),
                    atualizado.getNome(),
                    atualizado.getEmail(),
                    atualizado.getTelefone(),
                    atualizado.getTipo(),
                    (atualizado instanceof Funcionario f) ? f.getCpf() : null,
                    (atualizado instanceof Corretor c) ? c.getCreci() : null
            );

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> listarUsuariosPorTipo(@PathVariable String tipo) {
        try {
            List<Usuario> usuarios;
            if ("visitante".equalsIgnoreCase(tipo)) {
                usuarios = usuarioService.findByTipo(Visitante.class);
            } else if ("funcionario".equalsIgnoreCase(tipo)) {
                usuarios = usuarioService.findByTipo(Funcionario.class);
            } else {
                return ResponseEntity.badRequest().body("Tipo inválido.");
            }

            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado.");
            }

            List<UsuarioResponseDTO> dtoList = usuarios.stream()
                    .map(usuario -> new UsuarioResponseDTO(
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getTelefone(),
                            usuario.getTipo(),
                            (usuario instanceof Funcionario f) ? f.getCpf() : null,
                            (usuario instanceof Corretor c) ? c.getCreci() : null
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar usuários: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok("Usuário excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir usuário: " + e.getMessage());
        }
    }
}