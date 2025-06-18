package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

@Data
public class EnderecoRequest {
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
