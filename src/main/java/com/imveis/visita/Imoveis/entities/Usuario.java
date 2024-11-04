package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "usuario")
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "LOGIN", unique = true, nullable = false)
    private String login;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @Column(name = "TELEFONE")
    private String telefone;

}
