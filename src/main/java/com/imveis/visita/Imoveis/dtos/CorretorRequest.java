package com.imveis.visita.Imoveis.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CorretorRequest {
    @NotBlank
    private String nome;

    @NotBlank
    private String login;

    @NotBlank
    private String senha;

    private String telefone;

    @NotBlank
    private String creci;
}
