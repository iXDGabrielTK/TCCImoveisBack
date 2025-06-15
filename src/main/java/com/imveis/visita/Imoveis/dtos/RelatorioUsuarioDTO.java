package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioUsuarioDTO {
    private Long idUsuario;
    private String nomeUsuario;
    private long quantidadeAcessos;
}
