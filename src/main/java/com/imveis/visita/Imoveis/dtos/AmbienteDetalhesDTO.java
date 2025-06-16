package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.AmbienteVistoria;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set; // Mudar de List para Set
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbienteDetalhesDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Set<FotoVistoriaDTO> fotos; // Mudar de List para Set

    public AmbienteDetalhesDTO(AmbienteVistoria ambiente) {
        this.id = ambiente.getId();
        this.nome = ambiente.getNome();
        this.descricao = ambiente.getDescricao();
        if (ambiente.getFotos() != null) {
            this.fotos = ambiente.getFotos().stream()
                    .map(FotoVistoriaDTO::new)
                    .collect(Collectors.toSet()); // Mudar para Collectors.toSet()
        } else {
            this.fotos = Set.of();
        }
    }
}