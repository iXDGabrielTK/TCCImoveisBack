package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.CorretorResponse;
import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final AuthService authService;

    @Transactional
    public Corretor criarCorretorAPartirDeUsuario(Usuario usuario, String creci) {
        if (corretorRepository.findByCreci(creci).isPresent()) {
            throw new IllegalArgumentException("CRECI já está cadastrado.");
        }

        if (corretorRepository.findById(usuario.getId()).isPresent()) {
            throw new IllegalArgumentException("Usuário já é corretor.");
        }

        Corretor corretor = new Corretor();
        corretor.setId(usuario.getId());
        corretor.setNome(usuario.getNome());
        corretor.setEmail(usuario.getEmail());
        corretor.setSenha(usuario.getSenha());
        corretor.setTelefone(usuario.getTelefone());
        corretor.setCreci(creci);

        Corretor salvo = corretorRepository.save(corretor);

        authService.atribuirRoleParaUsuario(salvo, "ROLE_CORRETOR");

        return salvo;
    }

    public Optional<CorretorResponse> buscarPorUsuarioId(Long usuarioId) {
        return corretorRepository.findById(usuarioId)
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
