package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Proposta;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@Data
@NoArgsConstructor

public class PropostaResponse {
    private BigInteger id;
    private BigDecimal rendaMensal;
    private BigDecimal entrada;
    private BigDecimal valorImovel;
    private BigDecimal valorFinanciamento;
    private LocalDate dataProposta;

    public PropostaResponse(Proposta proposta) {
        this.id = BigInteger.valueOf(proposta.getId());
        this.rendaMensal = proposta.getRendaMensal();
        this.entrada = proposta.getEntrada();
        this.valorImovel = proposta.getValorImovel();
        this.valorFinanciamento = proposta.getValorFinanciamento();
        this.dataProposta = proposta.getDataProposta();
    }

    // Getters
}

