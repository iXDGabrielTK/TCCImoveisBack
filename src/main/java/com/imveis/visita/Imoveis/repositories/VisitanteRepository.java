package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, BigInteger> {
    @SuppressWarnings("unused")
    Optional<Visitante> findByEmailAndSenha(String email, String senha);
}
