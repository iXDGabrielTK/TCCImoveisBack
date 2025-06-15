package com.imveis.visita.Imoveis.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VisitantTest {

    @Test
    void testVisitanteCreation() {
        Visitante visitante = new Visitante();
        assertNull(visitante.getId());
        assertNull(visitante.getNome());
        assertNull(visitante.getEmail());
        assertNull(visitante.getSenha());
        assertNull(visitante.getTelefone());
        assertNull(visitante.getDataAcesso());
        assertNull(visitante.getDataCadastro());
        assertNotNull(visitante.getRoles());
        assertTrue(visitante.getRoles().isEmpty());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("VISITANTE"));

        visitante.setId(1L);
        visitante.setNome("Ana Pereira");
        visitante.setEmail("ana.pereira");
        visitante.setSenha("senha789");
        visitante.setTelefone("11987654321");
        visitante.setRoles(roles);
        visitante.setDataAcesso(now);
        visitante.setDataCadastro(yesterday);

        assertEquals(1L, visitante.getId());
        assertEquals("Ana Pereira", visitante.getNome());
        assertEquals("ana.pereira", visitante.getEmail());
        assertEquals("senha789", visitante.getSenha());
        assertEquals("11987654321", visitante.getTelefone());
        assertEquals(roles, visitante.getRoles());
        assertEquals(now, visitante.getDataAcesso());
        assertEquals(yesterday, visitante.getDataCadastro());
    }

    @Test
    void testSettersAndGetters() {
        Visitante visitante = new Visitante();
        LocalDateTime now = LocalDateTime.now();

        visitante.setDataAcesso(now);
        assertEquals(now, visitante.getDataAcesso());

        LocalDateTime yesterday = now.minusDays(1);
        visitante.setDataCadastro(yesterday);
        assertEquals(yesterday, visitante.getDataCadastro());

        visitante.setId(123L);
        visitante.setNome("Carlos Oliveira");
        visitante.setEmail("carlos.oliveira");
        visitante.setSenha("senha101112");
        visitante.setTelefone("11912345678");

        assertEquals(123L, visitante.getId());
        assertEquals("Carlos Oliveira", visitante.getNome());
        assertEquals("carlos.oliveira", visitante.getEmail());
        assertEquals("senha101112", visitante.getSenha());
        assertEquals("11912345678", visitante.getTelefone());

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("USER"));
        visitante.setRoles(roles);
        assertEquals(roles, visitante.getRoles());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);

        Visitante visitante = new Visitante();
        visitante.setDataAcesso(now);
        visitante.setDataCadastro(yesterday);

        String toString = visitante.toString();

        assertTrue(toString.contains("dataAcesso"));
        assertTrue(toString.contains("dataCadastro"));
    }


}
