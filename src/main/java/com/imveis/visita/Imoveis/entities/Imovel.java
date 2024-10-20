package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Imovel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Imovel {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImovel;

    @Column(name = "TIPO_IMOVEL")
    private String tipoImovel;

    @Column(name = "DESCRICAO_IMOVEL")
    private String descricaoImovel;

    @Column(name = "STATUS_IMOVEL")
    private Boolean statusImovel;

    @Column(name = "TAMANHO_IMOVEL")
    private Float tamanhoImovel;

    @Column(name = "PRECO_IMOVEL")
    private Float precoImovel;

    @ManyToOne
    @JoinColumn(name = "ENDERECO_ID")
    private Endereco enderecoImovel;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    private List<Vistoria> vistorias;

    @Column(name = "HISTORICO_MANUTENCAO")
    private String historicoManutencao;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

}
