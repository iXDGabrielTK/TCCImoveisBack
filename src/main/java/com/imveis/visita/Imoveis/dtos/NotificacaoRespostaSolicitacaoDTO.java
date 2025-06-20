package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificacaoRespostaSolicitacaoDTO extends NotificacaoDTO {
    private String tipo;
    private String resumo;
    private LocalDateTime dataCriacao;
    private boolean lida;
    private boolean arquivada;
}

