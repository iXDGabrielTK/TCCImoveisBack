package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Vistoria;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class VistoriaDTO {
    private BigInteger idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private LocalDate dataVistoria;

    public VistoriaDTO(Vistoria vistoria){
        this.idVistoria = vistoria.getIdVistoria();
        this.tipoVistoria = vistoria.getTipoVistoria();
        this.laudoVistoria = vistoria.getLaudoVistoria();
        this.dataVistoria = vistoria.getDataVistoria();
    }
}
