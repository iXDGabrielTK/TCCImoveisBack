package com.imveis.visita.Imoveis.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class VistoriaRequest {
    private Long idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private LocalDate dataVistoria;
    private Long usuarioId;
    private String numero;
    private EnderecoDTO endereco;
    private Long imovelId;
    private List<AmbienteVistoriaRequest> ambientes;

}
