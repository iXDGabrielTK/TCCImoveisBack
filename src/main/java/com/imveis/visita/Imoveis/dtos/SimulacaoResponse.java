package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SimulacaoResponse {

    private BigDecimal valorMaxParcela;
    private BigDecimal valorFinanciamento;
    private BigDecimal poderDeCompra;
    private String mensagem; // Novo campo opcional

    public SimulacaoResponse(BigDecimal valorMaxParcela, BigDecimal valorFinanciamento, BigDecimal poderDeCompra) {
        this.valorMaxParcela = valorMaxParcela;
        this.valorFinanciamento = valorFinanciamento;
        this.poderDeCompra = poderDeCompra;
    }

    public SimulacaoResponse(String mensagem) {
        this.mensagem = mensagem;
    }
}
