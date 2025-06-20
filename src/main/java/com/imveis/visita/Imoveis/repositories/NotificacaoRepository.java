package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Notificacao;
import com.imveis.visita.Imoveis.entities.NotificacaoProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatarioIdAndLidaFalse(Long destinatarioId);

    List<Notificacao> findByVisivelParaTodosFuncionariosTrueAndLidaFalse();

    List<Notificacao> findAllByOrderByDataCriacaoDesc();

    @Query("SELECT np FROM NotificacaoProposta np JOIN FETCH np.proposta p JOIN FETCH p.usuario u JOIN FETCH p.imovel i WHERE np.id = :id")
    NotificacaoProposta findNotificacaoPropostaWithAll(@Param("id") Long id);
}
