package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@DiscriminatorValue("corretor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Corretor extends Usuario {

    @Column(name = "CRECI", nullable = false, unique = true)
    private String creci;

    @ManyToMany(mappedBy = "corretores")
    private List<Imovel> imoveis;
}
