package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "corretor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Corretor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "creci", nullable = false, unique = true)
    private String creci;

    // Se quiser depois:
    // @ManyToMany(mappedBy = "corretores")
    // private List<Imovel> imoveis;
}
