package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FavoritoDTO {
    private Long idImovel;
    private String tipoImovel;
    private Float precoImovel;
    private String descricaoImovel;
    private EnderecoDTO enderecoImovel;
    private List<FotoImovelDTO> fotosImovel;
}
