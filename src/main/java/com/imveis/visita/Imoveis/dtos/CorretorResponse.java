package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

@Data
public class CorretorResponse {
    private Long id;
    private String nome;
    private String login;
    private String telefone;
    private String creci;
}
