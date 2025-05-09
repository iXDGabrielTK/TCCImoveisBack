package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final AuthService authService;
    private final VisitanteService visitanteService;
    private final UsuarioRepository usuarioRepository;


    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, AuthService authService, VisitanteService visitanteService) {
        this.usuarioRepository = usuarioRepository;
        this.authService = authService;
        this.visitanteService = visitanteService;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(BigInteger id) {
        return usuarioRepository.findById(id);
    }

    public Usuario buscarPorId(BigInteger id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }


    public Usuario save(Usuario usuario) {
        authService.encodePassword(usuario);

        if (usuario instanceof Visitante visitante) {
            if (visitante.getDataCadastro() == null) {
                visitante.setDataCadastro(LocalDateTime.now());
            }
            return visitanteService.save(visitante);
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);

        if (savedUsuario instanceof Funcionario) {
            authService.atribuirRoleParaUsuario(savedUsuario, "FUNCIONARIO");
        } else {
            authService.atribuirRoleParaUsuario(savedUsuario, "VISITANTE");
        }

        return savedUsuario;
    }


    public void deleteById(BigInteger id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> findByTipo(Class<? extends Usuario> tipo) {
        return usuarioRepository.findByTipo(tipo);
    }
}
