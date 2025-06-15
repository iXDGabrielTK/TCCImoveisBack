package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.NotificacaoDTO;
import com.imveis.visita.Imoveis.entities.Proposta;
import com.imveis.visita.Imoveis.entities.Usuario;

import java.util.List;

public interface NotificacaoService {
    void notificarCorretor(String nome, String creci, Usuario destinatario);

    void notificarImobiliaria(String nomeCorretor, String nomeImobiliaria, String cnpj, Usuario destinatario);

    List<NotificacaoDTO> listarNaoLidas(Long usuarioId);

    void marcarComoLida(Long id);


    void aprovarSolicitacaoCorretor(Long id);

    void recusarSolicitacaoCorretor(Long id);

    void aprovarSolicitacaoImobiliaria(Long id);

    void recusarSolicitacaoImobiliaria(Long id);

    void criarNotificacaoProposta(Proposta proposta, Usuario destinatario);

}