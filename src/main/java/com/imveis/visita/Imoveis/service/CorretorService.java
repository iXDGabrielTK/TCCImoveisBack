package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.CorretorResponse;
import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthService authService; // Para atribuir ROLE_CORRETOR

    @Transactional
    public CorretorResponse candidatarUsuarioParaCorretor(BigInteger usuarioId, String creci) {
        if (corretorRepository.findByCreci(creci).isPresent()) {
            throw new IllegalArgumentException("CRECI já está cadastrado.");
        }

        if (corretorRepository.findByUsuarioId(usuarioId).isPresent()) {
            throw new IllegalArgumentException("Usuário já é corretor.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Corretor corretor = new Corretor();
        corretor.setUsuario(usuario);
        corretor.setCreci(creci);

        Corretor salvo = corretorRepository.save(corretor);
        authService.atribuirRoleParaUsuario(usuario, "ROLE_CORRETOR");

        return toResponse(salvo);
    }

    public Optional<CorretorResponse> buscarPorUsuarioId(BigInteger usuarioId) {
        return corretorRepository.findByUsuarioId(usuarioId)
                .map(corretor -> {
                    CorretorResponse response = new CorretorResponse();
                    response.setId(corretor.getId());
                    response.setCreci(corretor.getCreci());
                    return response;
                });
    }


    private CorretorResponse toResponse(Corretor corretor) {
        CorretorResponse response = new CorretorResponse();
        response.setId(corretor.getId());
        response.setCreci(corretor.getCreci());

        return response;
    }


    public List<CorretorResponse> buscarTodos() {
        List<Corretor> corretores = corretorRepository.findAll();
        return corretores.stream()
                .map(this::toResponse)
                .toList();
    }
}
