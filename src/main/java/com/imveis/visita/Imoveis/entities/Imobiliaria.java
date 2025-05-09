package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToMany(mappedBy = "imobiliarias")
    private List<Imovel> imoveis;

}