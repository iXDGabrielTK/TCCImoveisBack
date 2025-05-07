package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
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

    @Column(name = "CNPJ", nullable = false, unique = true)
    private String cnpj;

    @ManyToMany(mappedBy = "imobiliarias")
    private List<Imovel> imovels;
}
