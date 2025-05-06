package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;


@Entity
@Table(name = "fotos_imovel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class FotoImovel {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idFotosImovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id")
    @ToString.Exclude
    private Imovel imovel;

    @Column(name = "url_foto_imovel", nullable = false, length = 1000)
    private String urlFotoImovel;
}
