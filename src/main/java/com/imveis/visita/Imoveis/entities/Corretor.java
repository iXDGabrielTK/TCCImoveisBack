package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "corretor")
@DiscriminatorValue("CORRETOR") // Hibernate
public class Corretor extends Usuario {

    @Column(name = "creci", nullable = false, unique = true)
    private String creci;

    @OneToMany(mappedBy = "corretor")
    private List<Imobiliaria> imobiliarias;

    @Override
    public String getTipo() {
        return "CORRETOR";
    }
}
