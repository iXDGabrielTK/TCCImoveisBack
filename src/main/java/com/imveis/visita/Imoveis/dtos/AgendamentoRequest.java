package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class AgendamentoRequest {
    private BigInteger imovelId;
    private String nomeVisitante;
    private LocalDate dataAgendamento;
    private BigInteger usuarioId;
    private Boolean horarioMarcado;
}
