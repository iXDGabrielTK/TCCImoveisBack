package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Getter
@Setter
public class RelatorioAgendamentoDTO {
    private BigInteger idImovel;
    private String descricaoImovel;
    private long totalAgendamentos;

    public RelatorioAgendamentoDTO(BigInteger idImovel, String descricaoImovel, long totalAgendamentos) {
        this.idImovel = idImovel;
        this.descricaoImovel = descricaoImovel;
        this.totalAgendamentos = totalAgendamentos;
    }

    // Getters e setters
}