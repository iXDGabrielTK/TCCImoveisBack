package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class VistoriaRequest {
    private BigInteger idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private LocalDate dataVistoria;
    private List<String> fotosVistoria;
    private String rua;
    private String numero;
    private String bairro;
}
