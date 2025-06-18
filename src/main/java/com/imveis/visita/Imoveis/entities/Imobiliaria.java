package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "imobiliaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imobiliaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "RAZAO_SOCIAL", nullable = false)
    private String razaoSocial;

    @Column(name = "CNPJ", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "CEP", nullable = false)
    private String cep;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean aprovada;

    @ManyToOne(optional = false)
    @JoinColumn(name = "corretor_id", nullable = false)
    private Corretor corretor;

    @ManyToMany(mappedBy = "imobiliarias")
    @JsonBackReference(value = "imovel-imobiliaria")
    private List<Imovel> imoveis;

}