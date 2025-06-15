package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAgendamentoDTO {
    private Long idImovel;
    private String descricaoImovel;
    private long quantidadeAgendamentos;
}
