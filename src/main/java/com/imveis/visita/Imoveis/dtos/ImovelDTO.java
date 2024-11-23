package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.entities.Imovel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ImovelDTO {
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private Endereco enderecoImovel;
    private List<String> fotosImovel;

    public ImovelDTO(Imovel imovel) {
        this.tipoImovel = imovel.getTipoImovel();
        this.descricaoImovel = imovel.getDescricaoImovel();
        this.statusImovel = imovel.getStatusImovel();
        this.tamanhoImovel = imovel.getTamanhoImovel();
        this.precoImovel = imovel.getPrecoImovel();
        this.enderecoImovel = imovel.getEnderecoImovel();
        this.fotosImovel = imovel.getFotosImovel()
                .stream()
                .map(FotoImovel::getUrlFotoImovel)
                .toList();
    }
}

