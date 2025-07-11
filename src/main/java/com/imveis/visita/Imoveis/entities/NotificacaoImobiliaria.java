package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@DiscriminatorValue("IMOBILIARIA")
public class NotificacaoImobiliaria extends Notificacao {

    @Column(length = 100)
    private String nomeCorretor;

    @Column()
    private String nomeImobiliaria;

    @Column(length = 18)
    private String cnpj;
    private Boolean aprovada;
    private Boolean respondida;
    private LocalDateTime dataResposta;

    @ManyToOne
    private Corretor remetente;

    @Override
    public String getResumo() {
        return nomeCorretor + " deseja cadastrar a imobiliária \"" + nomeImobiliaria + "\" (CNPJ: " + cnpj + ")";
    }

    public boolean isAprovadaOuRecusada() {
        return Boolean.TRUE.equals(respondida);
    }

}
