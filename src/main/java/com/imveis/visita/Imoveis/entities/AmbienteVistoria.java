package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "ambientes_vistoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"vistoria", "fotos"}) // Adicionado para excluir relações
public class AmbienteVistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_ambiente", length = 500) // Exemplo: aumentar para 500
    private String nome;

    // Use TEXT para descrições, pois elas são as mais prováveis de serem longas
    @Column(name = "descricao_ambiente", columnDefinition = "TEXT")
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "vistoria_id", nullable = false)
    private Vistoria vistoria;

    @OneToMany(mappedBy = "ambiente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FotoVistoria> fotos; // Mude de List para Set
}
