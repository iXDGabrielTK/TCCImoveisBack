package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class PropostaRequest {

    private BigDecimal rendaMensal;
    private BigDecimal entrada;
    private BigDecimal valorImovel;
    private BigInteger idImovel;
}
