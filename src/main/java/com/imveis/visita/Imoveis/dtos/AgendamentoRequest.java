package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AgendamentoRequest {
    private Long imovelId;
    private String nomeVisitante;
    private LocalDate dataAgendamento;
    private Long usuarioId;
    private Boolean horarioMarcado;
}
