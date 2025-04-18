package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("VISITANTE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Visitante extends Usuario {

    @Column(name = "DATA_ACESSO")
    private LocalDateTime dataAcesso;

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;

}
