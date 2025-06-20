package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFavoritoDTO {
    private Long idImovel;
    private String descricaoImovel;
    private Long quantidadeFavoritos; // Usar Long ou Integer para a contagem
}