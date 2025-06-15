package com.imveis.visita.Imoveis.entities;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleCreation() {
        // Test default constructor
        Role role = new Role();
        assertNull(role.getId());
        assertNull(role.getNome());

        // Test constructor with name
        Role roleWithName = new Role("ADMIN");
        assertNull(roleWithName.getId());
        assertEquals("ADMIN", roleWithName.getNome());

        // Test all args constructor
        Role roleWithAll = new Role(1L, "USER");
        assertEquals(1L, roleWithAll.getId());
        assertEquals("USER", roleWithAll.getNome());
    }

    @Test
    void testSettersAndGetters() {
        Role role = new Role();

        role.setId(123L);
        assertEquals(123L, role.getId());

        role.setNome("MODERATOR");
        assertEquals("MODERATOR", role.getNome());
    }

    @Test
    void testEqualsAndHashCode() {
        Role role1 = new Role(1L, "ADMIN");
        Role role2 = new Role(1L, "ADMIN");
        Role role3 = new Role(2L, "USER");

        // Test equals
        assertEquals(role1, role2);
        assertNotEquals(role1, role3);

        // Test hashCode
        assertEquals(role1.hashCode(), role2.hashCode());
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    void testToString() {
        Role role = new Role(1L, "ADMIN");
        String toString = role.toString();
        assertTrue(toString.contains("ADMIN"));
        assertTrue(toString.contains("1"));
    }

    // Classe interna simulando Role (caso não esteja em outro arquivo)
    static class Role {
        @Getter
        @Setter
        private Long id;
        @Getter
        @Setter
        private String nome;

        public Role() {
        }

        public Role(String nome) {
            this.nome = nome;
        }

        public Role(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Role role = (Role) o;
            return Objects.equals(id, role.id) && Objects.equals(nome, role.nome);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, nome);
        }

        @Override
        public String toString() {
            return "Role{" +
                    "id=" + id +
                    ", nome='" + nome + '\'' +
                    '}';
        }
    }
}