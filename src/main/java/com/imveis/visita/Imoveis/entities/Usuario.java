package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity(name = "Usuario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Usuario {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idUsuario;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "TELEFONE")
    private String telefone;

}
