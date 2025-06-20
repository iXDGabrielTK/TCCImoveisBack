package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
@DiscriminatorValue("CORRETOR")
public class Corretor extends Usuario {

    @Column(name = "creci", length = 15, nullable = false, unique = true)
    @Pattern(regexp = "CRECI-[A-Z]{2} \\d{1,6}(-[A-Z])?")
    private String creci;

    @OneToMany(mappedBy = "corretor")
    private List<Imobiliaria> imobiliarias;

    @Override
    public String getTipo() {
        return "CORRETOR";
    }
}
