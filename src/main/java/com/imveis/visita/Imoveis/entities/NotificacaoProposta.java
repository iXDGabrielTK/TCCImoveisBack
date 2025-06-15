package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("PROPOSTA")
public class NotificacaoProposta extends Notificacao {

    @ManyToOne(optional = false)
    @JoinColumn(name = "proposta_id", referencedColumnName = "id")
    private Proposta proposta;

    @Override
    public String getResumo() {
        return proposta.getUsuario().getNome()
                + " enviou uma proposta de R$ "
                + proposta.getValorFinanciamento()
                + " para o imóvel ID "
                + proposta.getImovel().getIdImovel();
    }

}
