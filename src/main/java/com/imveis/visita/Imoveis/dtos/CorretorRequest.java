package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CorretorRequest {
    private BigInteger usuarioId;
    private String creci;
}

