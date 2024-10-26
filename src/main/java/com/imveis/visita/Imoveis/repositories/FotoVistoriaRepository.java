package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FotoVistoriaRepository extends JpaRepository<FotoVistoria, BigInteger> {
}
