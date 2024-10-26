package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

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

   @ElementCollection // Se for apenas uma lista de URLs
    private List<String> urlFotoImovel;

}
