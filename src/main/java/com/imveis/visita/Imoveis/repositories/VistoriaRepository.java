package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Vistoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface VistoriaRepository extends JpaRepository<Vistoria, BigInteger> {
}
