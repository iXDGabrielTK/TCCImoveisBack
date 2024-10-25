package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface EnderecoRepository extends JpaRepository<Endereco, BigInteger> {
}
