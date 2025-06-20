package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collections; // Importar Collections
import java.util.Set;

@Data
@Getter
@Setter
public class RelatorioVistoriaDTO {
    private Long idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private LocalDate dataVistoria;

    private Long idImovel;
    private String descricaoImovel;

    private ImovelDTO imovel;
    private Set<AmbienteDetalhesDTO> ambientes;

    public RelatorioVistoriaDTO(Long idVistoria, Long idImovel, String descricaoImovel, LocalDate dataVistoria, String laudoVistoria) {
        this.idVistoria = idVistoria;
        this.idImovel = idImovel;
        this.descricaoImovel = descricaoImovel;
        this.dataVistoria = dataVistoria;
        this.laudoVistoria = laudoVistoria;

        this.tipoVistoria = null;
        this.imovel = null;
        this.ambientes = Collections.emptySet();
    }

}