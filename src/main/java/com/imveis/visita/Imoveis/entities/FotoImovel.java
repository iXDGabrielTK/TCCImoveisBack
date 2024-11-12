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
@Getter

public class FotoImovel {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idFotosImovel;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    @Column(name = "url_foto_imovel", nullable = false)
    private String urlFotoImovel;
}
