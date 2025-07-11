package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private Long idFotosImovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id")
    @ToString.Exclude
    @JsonIgnore
    private Imovel imovel;

    @Column(name = "url_foto_imovel", nullable = false, length = 1000)
    private String urlFotoImovel;
}
