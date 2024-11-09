// src/main/java/com/imveis/visita/Imoveis/repositories/FuncionarioRepository.java
package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, BigInteger> {
    Optional<Funcionario> findByLogin(String login);

}
