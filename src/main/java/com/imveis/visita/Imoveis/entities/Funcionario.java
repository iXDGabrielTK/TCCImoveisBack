// src/main/java/com/imveis/visita/Imoveis/entities/Funcionario.java
package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "funcionario")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario extends UsuarioBase {
    private String cpf;
}
