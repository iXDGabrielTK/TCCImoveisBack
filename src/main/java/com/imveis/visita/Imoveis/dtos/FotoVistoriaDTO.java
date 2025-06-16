package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoVistoriaDTO {
    private Long id;
    private String urlFotoVistoria;

    public FotoVistoriaDTO(FotoVistoria foto) {
        this.id = foto.getId();
        this.urlFotoVistoria = foto.getUrlFotoVistoria();
    }
}