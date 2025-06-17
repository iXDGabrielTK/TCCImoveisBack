package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Endereco;
import lombok.Data;

import java.util.List;

@Data
public class ImovelRequest {
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private String historicoManutencao;
    private List<FotoImovelDTO> fotosImovel; // <--- CORRIGIDO: Agora usa FotoImovelDTO
    private Endereco enderecoImovel;
    private List<Long> idsCorretores;
    private List<Long> idsImobiliarias;
}