package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "visitante")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Visitante extends Usuario {

    private String documentoIdentidade;

}
