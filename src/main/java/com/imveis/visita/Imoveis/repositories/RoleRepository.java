package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, BigInteger> {
    Optional<Role> findByNome(String nome);

    boolean existsByNome(String nome);
}