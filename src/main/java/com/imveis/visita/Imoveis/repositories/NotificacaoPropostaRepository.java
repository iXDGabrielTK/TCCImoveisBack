package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.NotificacaoProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificacaoPropostaRepository extends JpaRepository<NotificacaoProposta, Long> {

}
