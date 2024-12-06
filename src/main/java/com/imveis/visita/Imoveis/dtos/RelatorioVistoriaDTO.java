package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter
@Setter
public class RelatorioVistoriaDTO {
    private BigInteger idVistoria;
    private BigInteger idImovel;
    private String descricaoImovel;
    private Date dataVistoria;
    private String laudoVistoria;

    public RelatorioVistoriaDTO(BigInteger idVistoria, BigInteger idImovel, String descricaoImovel, Date dataVistoria, String laudoVistoria) {
        this.idVistoria = idVistoria;
        this.idImovel = idImovel;
        this.descricaoImovel = descricaoImovel;
        this.dataVistoria = dataVistoria;
        this.laudoVistoria = laudoVistoria;
    }

    // Getters e Setters
}