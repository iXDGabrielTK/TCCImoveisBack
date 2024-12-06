package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Getter
@Setter
public class RelatorioAgendamentoDTO {
    private BigInteger idImovel;
    private String descricaoImovel;
    private long quantidadeAgendamentos;

    public RelatorioAgendamentoDTO(BigInteger idImovel, String descricaoImovel, long quantidadeAgendamentos) {
        this.idImovel = idImovel;
        this.descricaoImovel = descricaoImovel;
        this.quantidadeAgendamentos = quantidadeAgendamentos;
    }

    // Getters e Setters
}