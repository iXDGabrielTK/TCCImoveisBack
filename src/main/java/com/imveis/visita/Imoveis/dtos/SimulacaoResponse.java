package com.imveis.visita.Imoveis.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
public class SimulacaoResponse {

    private BigDecimal valorMaxParcela;
    private BigDecimal valorFinanciamento;
    private BigDecimal poderDeCompra;

    public SimulacaoResponse(BigDecimal valorMaxParcela, BigDecimal valorFinanciamento, BigDecimal poderDeCompra) {
        this.valorMaxParcela = valorMaxParcela;
        this.valorFinanciamento = valorFinanciamento;
        this.poderDeCompra = poderDeCompra;
    }
}
