package com.imveis.visita.Imoveis.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificacaoImobiliariaDTO extends NotificacaoDTO {
    private String nomeCorretor;
    private String nomeImobiliaria;
    private String cnpj;

}
