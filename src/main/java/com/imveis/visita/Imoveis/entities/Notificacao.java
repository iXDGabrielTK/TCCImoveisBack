package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_notificacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    protected Usuario destinatario;

    protected boolean lida = false;
    @Column(updatable = false)
    protected LocalDateTime dataCriacao = LocalDateTime.now();
    private boolean visivelParaTodosFuncionarios;

    public abstract String getResumo();

}
