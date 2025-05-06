package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAgendamentoDTO {
    private BigInteger idImovel;
    private String descricaoImovel;
    private long quantidadeAgendamentos;
}
