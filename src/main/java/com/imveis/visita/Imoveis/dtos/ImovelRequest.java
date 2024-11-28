package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Endereco;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Data
public class ImovelRequest {
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private Endereco enderecoImovel;
    private String urlFoto;

    // ALTERAÇÃO: Adicionando o campo funcionarioId
    private BigInteger funcionarioId;
}
