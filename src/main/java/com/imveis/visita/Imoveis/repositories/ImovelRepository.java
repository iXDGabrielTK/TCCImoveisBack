package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ImovelRepository extends JpaRepository<Imovel, BigInteger> {

}
