package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Imobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ImobiliariaRepository extends JpaRepository<Imobiliaria, BigInteger>{
    Optional<Imobiliaria> findByCnpj(String cnpj);
}