package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class RelatorioVistoriaDTO {
    private Long idVistoria;
    private Long idImovel;
    private String descricaoImovel;
    private LocalDate dataVistoria;
    private String laudoVistoria;

    public RelatorioVistoriaDTO(Long idVistoria, Long idImovel, String descricaoImovel, LocalDate dataVistoria, String laudoVistoria) {
        this.idVistoria = idVistoria;
        this.idImovel = idImovel;
        this.descricaoImovel = descricaoImovel;
        this.dataVistoria = dataVistoria;
        this.laudoVistoria = laudoVistoria;
    }
}