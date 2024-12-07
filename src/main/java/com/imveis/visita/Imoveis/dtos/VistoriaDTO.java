package com.imveis.visita.Imoveis.dtos;
import com.imveis.visita.Imoveis.entities.Vistoria;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class VistoriaDTO {
    private BigInteger idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private Date dataVistoria;
    private List<String> fotosVistoria;

    public VistoriaDTO(Vistoria vistoria){
        this.idVistoria = vistoria.getIdVistoria();
        this.tipoVistoria = vistoria.getTipoVistoria();
        this.laudoVistoria = vistoria.getLaudoVistoria();
        this.dataVistoria = vistoria.getDataVistoria();
        this.fotosVistoria = vistoria.getFotosvistoria()
                .stream()
                .flatMap(foto -> Arrays.stream(foto.getUrlFotoVistoria().split(",")))
                .map(String::trim)
                .toList();
    }
}
