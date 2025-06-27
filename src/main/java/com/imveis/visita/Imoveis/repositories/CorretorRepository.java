package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Corretor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorretorRepository extends JpaRepository<Corretor, Long> {

    Optional<Corretor> findByCreci(String creci);

    @EntityGraph(attributePaths = "imobiliarias")
    Optional<Corretor> findWithImobiliariasById(Long id);
}
