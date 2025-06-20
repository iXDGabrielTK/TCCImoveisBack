package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Endereco;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ImovelRequest {
    private String tipoImovel;
    private String descricaoImovel;
    private Boolean statusImovel;
    private Float tamanhoImovel;
    private Float precoImovel;
    private String historicoManutencao;
    private List<FotoImovelDTO> fotosImovel;
    private Endereco enderecoImovel;
    private List<Long> idsCorretores;
    private List<Long> idsImobiliarias;
    @NotNull(message = "O ID da imobiliária é obrigatório")
    private Long imobiliariaId;
}