package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CORRETOR")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NotificacaoCorretor extends Notificacao {

    @Column(length = 100)
    private String nomeSolicitante;
    private String creciSolicitado;

    private Boolean aprovada;
    private Boolean respondida;
    private LocalDateTime dataResposta;

    @ManyToOne
    private Usuario remetente;

    @Override
    public String getResumo() {
        return nomeSolicitante + " deseja se tornar corretor (CRECI: " + creciSolicitado + ")";
    }

    public boolean isAprovadaOuRecusada() {
        return Boolean.TRUE.equals(respondida);
    }

}
