package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ImovelDTO {
    private Long idImovel;
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private List<FotoImovelDTO> fotosImovel;
    private EnderecoDTO enderecoImovel;
    private String historicoManutencao;
    private List<String> nomesCorretores;
    private Long imobiliariaId;
    private String nomeImobiliaria;

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
                .map(foto -> new FotoImovelDTO(foto.getIdFotosImovel(), foto.getUrlFotoImovel()))
                .collect(Collectors.toList())
                : List.of();
        this.nomesCorretores = imovel.getCorretores() != null && Hibernate.isInitialized(imovel.getCorretores())
                ? imovel.getCorretores().stream()
                .map(Usuario::getNome)
                .toList()
                : List.of();

        this.imobiliariaId = imovel.getImobiliaria() != null ? imovel.getImobiliaria().getId() : null;
        this.nomeImobiliaria = imovel.getImobiliaria() != null ? imovel.getImobiliaria().getNome() : null;
    }
}