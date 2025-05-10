package com.imveis.visita.Imoveis.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class SimulacaoRequest {

    private BigDecimal rendaMensal;
    private BigDecimal valorEntrada;
    private int prazo;
}
