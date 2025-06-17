package com.imveis.visita.Imoveis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoImovelDTO {
    private Long id; // ID que o frontend envia
    private String urlFotoImovel; // URL da foto
}