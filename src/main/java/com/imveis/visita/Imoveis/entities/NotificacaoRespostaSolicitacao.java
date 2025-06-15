package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("RESPOSTA_SOLICITACAO")
@Getter
@Setter
public class NotificacaoRespostaSolicitacao extends Notificacao {

    private boolean aprovada;

    private String tipoSolicitacao;

    @Override
    public String getResumo() {
        return aprovada
                ? "Sua solicitação para se tornar " + tipoSolicitacao.toLowerCase() + " foi aprovada."
                : "Sua solicitação para se tornar " + tipoSolicitacao.toLowerCase() + " foi recusada.";
    }
}
