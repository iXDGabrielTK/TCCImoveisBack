package com.imveis.visita.Imoveis.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImobiliariaRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String razaoSocial;

    @NotBlank
    private String cnpj;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String cep;
}
