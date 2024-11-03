// src/main/java/com/imveis/visita/Imoveis/entities/Visitante.java
package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "visitante")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Visitante extends UsuarioBase {
    private String documentoIdentidade;
}
