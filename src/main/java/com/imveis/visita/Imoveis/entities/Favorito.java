package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorito")
@Getter
@Setter
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Visitante visitante;

    @ManyToOne(optional = false)
    private Imovel imovel;

    @Column(name = "data_favorito")
    private LocalDateTime dataFavorito = LocalDateTime.now();

}
