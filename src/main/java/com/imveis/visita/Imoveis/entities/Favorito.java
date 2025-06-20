package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorito", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "imovel_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_favorito")
    private LocalDateTime dataFavorito = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Imovel imovel;

}
