package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class RelatorioUsuarioDTO {
    private BigInteger idUsuario;
    private String nomeUsuario;
    private long quantidadeAcessos;
}
