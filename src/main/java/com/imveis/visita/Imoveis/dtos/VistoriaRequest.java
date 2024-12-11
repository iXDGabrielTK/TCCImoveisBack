package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@Data
public class VistoriaRequest {
    private BigInteger idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private LocalDate dataVistoria;
    private BigInteger usuarioId;
    private String rua;
    private String numero;
    private String bairro;
}
