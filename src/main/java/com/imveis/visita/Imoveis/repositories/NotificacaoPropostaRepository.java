package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.NotificacaoProposta;
import com.imveis.visita.Imoveis.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoPropostaRepository extends JpaRepository<NotificacaoProposta, Long> {
    List<NotificacaoProposta> findByDestinatario(Usuario usuario);
}
