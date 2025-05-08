package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@Data
@NoArgsConstructor
public class ImovelDTO {
    private BigInteger idImovel;
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private List<String> fotosImovel;
    private EnderecoDTO enderecoImovel;
    private String historicoManutencao;
    private List<String> nomesCorretores;
    private List<String> nomesImobiliarias;

    public ImovelDTO(Imovel imovel) {
        this.idImovel = imovel.getIdImovel();
        this.tipoImovel = imovel.getTipoImovel();
        this.descricaoImovel = imovel.getDescricaoImovel();
        this.statusImovel = imovel.getStatusImovel();
        this.tamanhoImovel = imovel.getTamanhoImovel();
        this.precoImovel = imovel.getPrecoImovel();
        this.historicoManutencao = imovel.getHistoricoManutencao();
        this.enderecoImovel = imovel.getEnderecoImovel() != null
                ? new EnderecoDTO(imovel.getEnderecoImovel())
                : null;
        this.fotosImovel = imovel.getFotosImovel() != null
                ? imovel.getFotosImovel().stream()
                .flatMap(foto -> Arrays.stream(foto.getUrlFotoImovel().split(",")))
                .map(String::trim)
                .toList()
                : List.of();
        this.nomesCorretores = imovel.getCorretores().stream().map(Usuario::getNome).toList();
        this.nomesImobiliarias = imovel.getImobiliarias().stream().map(Imobiliaria::getNome).toList();
    }
}
