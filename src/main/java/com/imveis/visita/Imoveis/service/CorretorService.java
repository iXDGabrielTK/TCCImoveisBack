package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.CorretorResponse;
import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Corretor criarCorretorAPartirDeUsuario(Usuario usuario, String creci) {
        try {
            if (corretorRepository.findByCreci(creci).isPresent()) {
                throw new IllegalArgumentException("CRECI já está cadastrado.");
            }
            if (corretorRepository.findById(usuario.getId()).isPresent()) {
                throw new IllegalArgumentException("Usuário já é corretor.");
            }

            // Atualiza o tipo do usuário
            Query query = entityManager.createNativeQuery("UPDATE usuario SET tipo_usuario = 'CORRETOR' WHERE id = :id");
            query.setParameter("id", usuario.getId());
            query.executeUpdate();

            // Cria entrada na tabela corretor
            Query insertCorretor = entityManager.createNativeQuery("INSERT INTO corretor (id, creci) VALUES (:id, :creci)");
            insertCorretor.setParameter("id", usuario.getId());
            insertCorretor.setParameter("creci", creci);
            insertCorretor.executeUpdate();

            // Remove roles antigas
            Query deleteRoles = entityManager.createNativeQuery("DELETE FROM usuario_roles WHERE usuario_id = :id");
            deleteRoles.setParameter("id", usuario.getId());
            deleteRoles.executeUpdate();

            // Atribui nova role (role_id 3 = corretor)
            Query insertRole = entityManager.createNativeQuery("INSERT INTO usuario_roles (usuario_id, role_id) VALUES (:id, 3)");
            insertRole.setParameter("id", usuario.getId());
            insertRole.executeUpdate();

            entityManager.clear(); // necessário para evitar cache incorreto
            Corretor corretor = entityManager.find(Corretor.class, usuario.getId());

            authService.atribuirRoleParaUsuario(corretor, "ROLE_CORRETOR");
            return corretor;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao promover usuário para corretor: " + e.getMessage(), e);
        }
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
