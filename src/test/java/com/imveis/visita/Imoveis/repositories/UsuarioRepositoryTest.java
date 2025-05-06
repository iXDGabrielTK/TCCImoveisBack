package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Role;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByTipo() {
        // Create a role
        Role role = new Role("USER");
        entityManager.persist(role);

        // Create a funcionario
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Carlos Oliveira");
        funcionario.setEmail("carlos.oliveira");
        funcionario.setSenha("senha789");
        funcionario.setTelefone("11987654321");
        funcionario.setCpf("11122233344");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        funcionario.setRoles(roles);

        entityManager.persist(funcionario);

        // Create a visitante
        Visitante visitante = new Visitante();
        visitante.setNome("Ana Pereira");
        visitante.setEmail("ana.pereira");
        visitante.setSenha("senha101112");
        visitante.setTelefone("11912345678");
        visitante.setDataAcesso(LocalDateTime.now());
        visitante.setDataCadastro(LocalDateTime.now().minusDays(1));
        visitante.setRoles(roles);

        entityManager.persist(visitante);
        entityManager.flush();

        // Test findByTipo for Funcionario
        List<Usuario> funcionarios = usuarioRepository.findByTipo(Funcionario.class);

        assertEquals(1, funcionarios.size());
        assertInstanceOf(Funcionario.class, funcionarios.getFirst());
        assertEquals("Carlos Oliveira", funcionarios.getFirst().getNome());

        // Test findByTipo for Visitante
        List<Usuario> visitantes = usuarioRepository.findByTipo(Visitante.class);

        assertEquals(1, visitantes.size());
        assertInstanceOf(Visitante.class, visitantes.getFirst());
        assertEquals("Ana Pereira", visitantes.getFirst().getNome());
    }
}
