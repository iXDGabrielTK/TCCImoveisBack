package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("FUNCIONARIO")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Funcionario extends Usuario {

    @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;

    // Construtor personalizado
    public Funcionario(String nome, String login, String senha, String telefone, String cpf) {
        super(null, nome, login, senha, telefone);
        this.cpf = cpf;
    }
}
