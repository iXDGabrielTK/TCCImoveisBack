package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String tipo;
    private String cpf;
    private Long idImobiliaria; // ADICIONE ESTA LINHA
}
