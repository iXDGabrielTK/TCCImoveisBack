package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    @SuppressWarnings("unused")
    Optional<Funcionario> findByEmailAndSenha(String email, String senha);
}
