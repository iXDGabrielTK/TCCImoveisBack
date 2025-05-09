package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificacaoProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Proposta proposta;

    @ManyToOne
    private Usuario destinatario;

    private boolean lida = false;

    private LocalDateTime dataEnvio = LocalDateTime.now();
}
