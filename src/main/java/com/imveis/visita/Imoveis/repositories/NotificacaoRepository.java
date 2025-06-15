package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatarioIdAndLidaFalse(Long destinatarioId);

    List<Notificacao> findByDestinatarioIdAndLidaFalseOrderByDataCriacaoDesc(Long destinatario_id);

}

