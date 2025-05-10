package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.entities.Imovel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

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
        this.nomesCorretores = imovel.getCorretores() != null && Hibernate.isInitialized(imovel.getCorretores())
                ? imovel.getCorretores().stream()
                .map(corretor -> corretor.getUsuario().getNome()) // 👈 acesso correto
                .toList()
                : List.of();

        this.nomesImobiliarias = imovel.getImobiliarias() != null && Hibernate.isInitialized(imovel.getImobiliarias())
                ? imovel.getImobiliarias().stream().map(Imobiliaria::getNome).toList()
                : List.of();
    }
}
