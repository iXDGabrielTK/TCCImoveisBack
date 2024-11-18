package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("FUNCIONARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Funcionario extends Usuario {

    @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;


}
