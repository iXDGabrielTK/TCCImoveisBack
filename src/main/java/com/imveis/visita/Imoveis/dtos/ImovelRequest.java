package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Endereco;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ImovelRequest {
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private String historicoManutencao;
    private List<String> fotosImovel;
    private Endereco enderecoImovel;
}
