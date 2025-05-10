package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("FUNCIONARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Funcionario extends Usuario {
    @Override
    public String getTipo() {
        return "FUNCIONARIO";
    }

    @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;


}