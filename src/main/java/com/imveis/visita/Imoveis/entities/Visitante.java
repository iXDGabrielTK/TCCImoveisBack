package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table (name = "visitante")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Visitante {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idVisitante;


    @ManyToOne
    private Usuario usuario;

   /* @JoinColumn(name = "LISTA_FAVORITO")
    @OneToMany
    private List<Imovel> listaFavorito;
   */

    @OneToMany
    @JoinTable(
            name = "Favoritos",
            joinColumns = @JoinColumn(name = "visitante_id"),
            inverseJoinColumns = @JoinColumn(name = "imovel_id")
    )
    private List<Imovel> listaFavorito;

}
