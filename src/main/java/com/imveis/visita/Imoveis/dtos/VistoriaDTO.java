package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Vistoria;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set; // Mudar de List para Set
import java.util.stream.Collectors;

@Getter
@Setter
public class VistoriaDTO {
    private Long idVistoria;
    private String tipoVistoria;
    private String laudoVistoria;
    private LocalDate dataVistoria;
    private ImovelDTO imovel; // Seu ImovelDTO existente
    private Set<AmbienteDetalhesDTO> ambientes; // Mudar de List para Set

    public VistoriaDTO(Vistoria vistoria){
        this.idVistoria = vistoria.getIdVistoria();
        this.tipoVistoria = vistoria.getTipoVistoria();
        this.laudoVistoria = vistoria.getLaudoVistoria();
        this.dataVistoria = vistoria.getDataVistoria();

        if (vistoria.getImovel() != null) {
            this.imovel = new ImovelDTO(vistoria.getImovel());
        } else {
            this.imovel = null;
        }

        if (vistoria.getAmbientes() != null) {
            this.ambientes = vistoria.getAmbientes().stream()
                    .map(AmbienteDetalhesDTO::new)
                    .collect(Collectors.toSet()); // Mudar para Collectors.toSet()
        } else {
            this.ambientes = Set.of(); // Usar Set.of() para Sets vazios
        }
    }
}