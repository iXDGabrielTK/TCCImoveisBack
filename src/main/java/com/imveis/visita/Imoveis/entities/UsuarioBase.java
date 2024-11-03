// src/main/java/com/imveis/visita/Imoveis/entities/UsuarioBase.java
package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class UsuarioBase {
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
