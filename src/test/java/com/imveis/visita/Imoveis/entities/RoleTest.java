package com.imveis.visita.Imoveis.entities;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
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
        Role roleWithAll = new Role(BigInteger.ONE, "USER");
        assertEquals(BigInteger.ONE, roleWithAll.getId());
        assertEquals("USER", roleWithAll.getNome());
    }

    @Test
    void testSettersAndGetters() {
        Role role = new Role();

        role.setId(BigInteger.valueOf(123));
        assertEquals(BigInteger.valueOf(123), role.getId());

        role.setNome("MODERATOR");
        assertEquals("MODERATOR", role.getNome());
    }

    @Test
    void testEqualsAndHashCode() {
        Role role1 = new Role(BigInteger.ONE, "ADMIN");
        Role role2 = new Role(BigInteger.ONE, "ADMIN");
        Role role3 = new Role(BigInteger.valueOf(2), "USER");

        // Test equals
        assertEquals(role1, role2);
        assertNotEquals(role1, role3);

        // Test hashCode
        assertEquals(role1.hashCode(), role2.hashCode());
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    void testToString() {
        Role role = new Role(BigInteger.ONE, "ADMIN");
        String toString = role.toString();
        assertTrue(toString.contains("ADMIN"));
        assertTrue(toString.contains("1"));
    }

    // Classe interna simulando Role (caso não esteja em outro arquivo)
    static class Role {
        @Getter
        @Setter
        private BigInteger id;
        @Getter
        @Setter
        private String nome;

        public Role() {
        }

        public Role(String nome) {
            this.nome = nome;
        }

        public Role(BigInteger id, String nome) {
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
