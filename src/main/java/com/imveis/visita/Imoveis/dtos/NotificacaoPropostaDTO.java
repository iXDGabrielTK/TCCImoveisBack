package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class NotificacaoPropostaDTO extends NotificacaoDTO {

    private Long idProposta;
    private Long idImovel;
    private BigDecimal valorProposta;
    private String nomeProponente;

}
