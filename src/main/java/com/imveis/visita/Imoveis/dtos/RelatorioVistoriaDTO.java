package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter
@Setter
public class RelatorioVistoriaDTO {
    private BigInteger idImovel;
    private String descricaoImovel;
    private String dataVistoria;
    private String laudoVistoria;

    public RelatorioVistoriaDTO(BigInteger idImovel, String descricaoImovel, String dataVistoria, String laudoVistoria) {
        this.idImovel = idImovel;
        this.descricaoImovel = descricaoImovel;
        this.dataVistoria = dataVistoria;
        this.laudoVistoria = laudoVistoria;
    }
}
