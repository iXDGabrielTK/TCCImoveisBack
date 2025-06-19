package com.imveis.visita.Imoveis.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public abstract class NotificacaoDTO {
    private Long id;
    private boolean lida;
    private boolean arquivada;
    private boolean respondida;
    private String resumo;
    private String tipo;
    private LocalDateTime dataCriacao;

    @JsonProperty("dataHora")
    public LocalDateTime getDataHora() {
        return dataCriacao;
    }
}
