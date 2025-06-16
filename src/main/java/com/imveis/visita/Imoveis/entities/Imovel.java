package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "imovel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"fotosImovel", "corretores", "imobiliarias", "vistorias"})
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
    private List<Vistoria> vistorias;

    @Column(name = "HISTORICO_MANUTENCAO")
    private String historicoManutencao;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FotoImovel> fotosImovel;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;

    @Column(name = "APAGADO", nullable = false)
    private Boolean apagado = false;

   @ManyToMany
    @JoinTable(
            name = "imovel_corretor",
            joinColumns = @JoinColumn(name = "id_imovel"),
            inverseJoinColumns = @JoinColumn(name = "id_corretor")
    )
   private Set<Corretor> corretores;



    @ManyToMany
    @JsonManagedReference(value = "imovel-imobiliaria")
    @JoinTable(
            name = "imovel_imobiliaria",
            joinColumns = @JoinColumn(name = "id_imovel"),
            inverseJoinColumns = @JoinColumn(name = "id_imobiliaria")
    )
    private List<Imobiliaria> imobiliarias;

}
