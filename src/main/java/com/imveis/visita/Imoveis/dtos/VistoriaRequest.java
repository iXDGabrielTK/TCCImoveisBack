package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
public class VistoriaRequest {
    private BigInteger idVistoria;
    private String laudoVistoria;
    private Date dataVistoria;
    private List<String> fotosVistoria;
    private String rua;
    private String numero;
    private String bairro;
}
