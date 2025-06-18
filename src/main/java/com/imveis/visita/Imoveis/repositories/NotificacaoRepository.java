package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatarioIdAndLidaFalse(Long destinatarioId);

    @Query("""
                SELECT n FROM Notificacao n
                WHERE (n.destinatario.id = :usuarioId OR n.visivelParaTodosFuncionarios = true)
                  AND n.arquivada = false
            """)
    List<Notificacao> findVisiveisParaUsuario(Long usuarioId);

    List<Notificacao> findByVisivelParaTodosFuncionariosTrueAndLidaFalse();
}

