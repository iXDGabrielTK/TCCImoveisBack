package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Imobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImobiliariaRepository extends JpaRepository<Imobiliaria, Long> {
    Optional<Imobiliaria> findByCnpj(String cnpj);

    @Query("SELECT i FROM Imobiliaria i WHERE i.corretor = :corretor AND i.aprovada = true")
    List<Imobiliaria> findByCorretorAndAprovadaTrue(@Param("corretor") Corretor corretor);

    @Query("SELECT i FROM Imobiliaria i WHERE i.aprovada = true")
    Optional<Imobiliaria> findByNome(@Param("nome") String nome);
}