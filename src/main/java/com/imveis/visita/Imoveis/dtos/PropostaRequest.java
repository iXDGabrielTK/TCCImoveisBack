package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Data
public class PropostaRequest {

    private BigDecimal rendaMensal;
    private BigDecimal entrada;
    private BigDecimal valorImovel;
    private BigInteger idImovel;
}
