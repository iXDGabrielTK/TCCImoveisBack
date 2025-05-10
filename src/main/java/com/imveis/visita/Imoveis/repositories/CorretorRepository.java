package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Corretor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface CorretorRepository extends JpaRepository<Corretor, Long> {

    Optional<Corretor> findByCreci(String creci);

    Optional<Corretor> findByUsuarioId(BigInteger usuarioId);

    Collection<? extends Corretor> findAllById(BigInteger id);
}
