package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity(name = "Funcionario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Funcionario {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idFuncionario;

    @ManyToOne
    private Usuario usuario;

    @Column(name = "CPF")
    private String cpf;

}
