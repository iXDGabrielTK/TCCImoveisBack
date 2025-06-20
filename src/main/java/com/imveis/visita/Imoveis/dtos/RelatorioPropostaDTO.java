package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPropostaDTO {
    private Long idProposta;
    private String nomeProponente;
    private Long idImovel;
    private String descricaoImovel;
    private BigDecimal valorImovel;
    private BigDecimal valorEntrada;
    private BigDecimal rendaMensal;
    private BigDecimal valorFinanciamento;
    private Integer prazo;
    private LocalDate dataProposta;
    private String statusProposta;
}