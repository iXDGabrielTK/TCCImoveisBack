package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificacaoCorretorDTO extends NotificacaoDTO {
    private String nomeSolicitante;
    private String creciSolicitado;
    private String nomeUsuario;
    private String emailUsuario;
    private boolean respondida;
    private boolean arquivada;

}
