package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
@Setter
@Getter
public class AgendamentoRequest {
    private String nomeVisitante;
    private BigInteger imovelId;
    private Date dataAgendamento;
}
