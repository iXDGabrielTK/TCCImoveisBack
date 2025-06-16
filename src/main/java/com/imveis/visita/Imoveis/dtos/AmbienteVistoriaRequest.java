package com.imveis.visita.Imoveis.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AmbienteVistoriaRequest {
    private Long id;
    private String nome;
    private String descricao;
    private List<FotoVistoriaDTO> fotosExistentes;
}
