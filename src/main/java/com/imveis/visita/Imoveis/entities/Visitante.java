package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("VISITANTE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Visitante extends Usuario {

    @Override
    public String getTipo() {
        return "VISITANTE";
    }

    @Column(name = "DATA_ACESSO")
    private LocalDateTime dataAcesso;

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;

}
