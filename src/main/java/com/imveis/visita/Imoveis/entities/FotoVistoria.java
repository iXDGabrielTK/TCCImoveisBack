package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table (name = "fotos_vistoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class FotoVistoria {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idFotosVistoria;

    @ManyToOne
    private Vistoria vistoria;

    @Column(name = "URL_FOTO_VISTORIA")
    private String urlFotoVistoria;
}
