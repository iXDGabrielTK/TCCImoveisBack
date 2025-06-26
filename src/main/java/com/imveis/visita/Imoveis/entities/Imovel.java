package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "imovel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"fotosImovel", "corretores", "vistorias", "agendamentos"})
public class Imovel {

    @Column(name = "id")
    @Id
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ENDERECO_ID", referencedColumnName = "id", nullable = false)
    private Endereco enderecoImovel;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Vistoria> vistorias = new ArrayList<>();

    @Column(name = "HISTORICO_MANUTENCAO")
    private String historicoManutencao;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<FotoImovel> fotosImovel = new ArrayList<>();

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Agendamento> agendamentos = new ArrayList<>();

    @Column(name = "APAGADO", nullable = false)
    private Boolean apagado = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imobiliaria_id", nullable = false)
    private Imobiliaria imobiliaria;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "imovel_corretor",
            joinColumns = @JoinColumn(name = "id_imovel"),
            inverseJoinColumns = @JoinColumn(name = "id_corretor")
    )
    @Builder.Default
    private Set<Corretor> corretores = new HashSet<>();
}