package com.imveis.visita.Imoveis.repositories;


import com.imveis.visita.Imoveis.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

}