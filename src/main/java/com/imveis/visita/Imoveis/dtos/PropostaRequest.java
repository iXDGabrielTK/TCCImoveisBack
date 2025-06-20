package com.imveis.visita.Imoveis.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropostaRequest {


    @NotNull(message = "ID do imóvel é obrigatório")
    private Long imovelId;

    @NotNull(message = "Valor de entrada é obrigatório")
    private BigDecimal entrada;

    @NotNull(message = "Renda mensal é obrigatória")
    private BigDecimal rendaMensal;

    @NotNull(message = "Número de parcelas é obrigatório")
    private Integer numeroParcelas;

    @NotNull(message = "Valor do imóvel é obrigatório")
    private BigDecimal valorImovel;

    private Long idImovel;
    private Integer prazo;

    private String observacoes;

}
