package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.entities.Vistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface FotoVistoriaRepository extends JpaRepository<FotoVistoria, BigInteger> {
    List<FotoVistoria> findByVistoria(Vistoria vistoria);
}






