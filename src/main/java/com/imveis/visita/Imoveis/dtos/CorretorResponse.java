package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CorretorResponse {
    private BigInteger id;
    private String creci;
}
