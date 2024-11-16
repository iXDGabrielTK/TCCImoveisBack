package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name = "fotos_vistoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FotoVistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "vistoria_id", nullable = false) // FK na tabela fotos_vistoria
    private Vistoria vistoria;

    @Column(name = "url_foto_vistoria", nullable = false, length = 1000)
    private String urlFotoVistoria;
}
