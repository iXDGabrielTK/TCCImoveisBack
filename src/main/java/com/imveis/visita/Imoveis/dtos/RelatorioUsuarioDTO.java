package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioUsuarioDTO {
    private BigInteger idUsuario;
    private String nomeUsuario;
    private long quantidadeAcessos;
}
