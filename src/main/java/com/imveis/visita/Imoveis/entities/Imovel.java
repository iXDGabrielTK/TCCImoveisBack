package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "imovel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Imovel {
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idImovel;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    private List<Visitante> visitantes;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ENDERECO_ID", referencedColumnName = "id")
    private Endereco enderecoImovel;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    private List<Vistoria> vistorias;

    @Column(name = "HISTORICO_MANUTENCAO")
    private String historicoManutencao;


    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FotoImovel> fotosImovel;

    @ManyToOne
    @JoinColumn(name = "FUNCIONARIO_ID")
    private Funcionario funcionario;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Agendamento> agendamentos;
}
