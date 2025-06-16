package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos_vistoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"ambiente"}) // Adicionado para excluir a relação
public class FotoVistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_foto_vistoria", nullable = false, length = 1000)
    private String urlFotoVistoria;

    @ManyToOne
    @JoinColumn(name = "ambiente_id", nullable = false)
    private AmbienteVistoria ambiente;

}
