package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table (name = "fotos_vistoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class FotoVistoria {
    //sdpidofds
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idFotosVistoria;

    @ManyToOne
    @JoinColumn(name = "vistoria_id", nullable = false)
    private Vistoria vistoria;

    @ElementCollection
    private List<String> urlFotoVistoria;
}
