package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class NotificacaoPropostaDTO extends NotificacaoDTO {

    private Long id;
    private String resumo;
    private boolean lida;
    private boolean arquivada;
    private LocalDateTime dataCriacao;

    private Long IdProposta;
    private Long IdImovel;
    private BigDecimal ValorProposta;
    private String nomeProponente;
    private String emailProponente;
    private String telefoneProponente;
}
