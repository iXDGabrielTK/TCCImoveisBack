// src/main/java/com/imveis/visita/Imoveis/repositories/VisitanteRepository.java
package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, BigInteger> {
    Visitante findByLogin(String login);
}
