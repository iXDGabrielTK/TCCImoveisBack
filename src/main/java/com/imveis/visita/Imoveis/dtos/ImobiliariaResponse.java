package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

@Data
public class ImobiliariaResponse {
    private Long id;
    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String email;
    private String cep;
}
