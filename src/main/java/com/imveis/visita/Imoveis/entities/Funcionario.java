package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "funcionario")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario extends Usuario {

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

}
