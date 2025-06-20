// ImovelDTO.java (ATUALIZADO)
package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Imobiliaria;
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
    private Float tamanhoImovel; // Considere Double
    private Float precoImovel;   // Considere Double
    private List<FotoImovelDTO> fotosImovel; // <--- AGORA É LISTA DE FOTOIMOVELDTO
    private EnderecoDTO enderecoImovel;
    private String historicoManutencao;
    private List<String> nomesCorretores;
    private List<String> nomesImobiliarias;
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
                // Mapeia para FotoImovelDTO, garantindo que o 'id' seja o idFotosImovel da entidade
                .map(foto -> new FotoImovelDTO(foto.getIdFotosImovel(), foto.getUrlFotoImovel())) // <--- CHAVE!
                .collect(Collectors.toList())
                : List.of();
        this.nomesCorretores = imovel.getCorretores() != null && Hibernate.isInitialized(imovel.getCorretores())
                ? imovel.getCorretores().stream()
                .map(Usuario::getNome)
                .toList()
                : List.of();

        this.nomesImobiliarias = imovel.getImobiliarias() != null && Hibernate.isInitialized(imovel.getImobiliarias())
                ? imovel.getImobiliarias().stream().map(Imobiliaria::getNome).toList()
                : List.of();
    }
}