package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Getter
@Setter
public class AgendamentoRequest {
    private BigInteger imovelId;
    private String nomeVisitante;
    private LocalDate dataAgendamento;
    private Boolean horarioMarcado;

    private BigInteger usuarioId;

}
