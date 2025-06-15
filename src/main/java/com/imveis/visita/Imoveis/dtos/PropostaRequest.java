package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropostaRequest {

    private BigDecimal rendaMensal;
    private BigDecimal entrada;
    private BigDecimal valorImovel;
    private Long idImovel;
}
