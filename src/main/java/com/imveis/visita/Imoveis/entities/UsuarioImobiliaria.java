package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("IMOBILIARIA_USER") // Valor para o campo discriminator_type na tabela usuario
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UsuarioImobiliaria extends Usuario {

    // Relacionamento OneToOne com Imobiliaria
    // Este usuário é o responsável principal por esta imobiliária no sistema
    @OneToOne
    @JoinColumn(name = "imobiliaria_id", unique = true) // Garante que uma imobiliária tem apenas um UsuarioImobiliaria
    private Imobiliaria imobiliaria;

    @Override
    public String getTipo() {
        return "IMOBILIARIA_USER";
    }
}