package com.imveis.visita.Imoveis.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class NotificacaoDTO {
    protected long id;
    protected boolean lida;
    protected String tipo;
    protected String resumo;
    protected LocalDateTime dataCriacao;
}
