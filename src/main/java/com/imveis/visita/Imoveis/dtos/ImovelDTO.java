package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.Imovel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
public class ImovelDTO {
    private BigInteger idImovel;
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private Endereco enderecoImovel;
    private List<String> fotosImovel;

    public ImovelDTO(Imovel imovel) {
        this.idImovel = imovel.getIdImovel();
        this.tipoImovel = imovel.getTipoImovel();
        this.descricaoImovel = imovel.getDescricaoImovel();
        this.statusImovel = imovel.getStatusImovel();
        this.tamanhoImovel = imovel.getTamanhoImovel();
        this.precoImovel = imovel.getPrecoImovel();
        this.enderecoImovel = imovel.getEnderecoImovel();
        this.fotosImovel = imovel.getFotosImovel()
                .stream()
                .flatMap(foto -> Arrays.stream(foto.getUrlFotoImovel().split(",")))
                .map(String::trim)
                .toList();
    }
}

