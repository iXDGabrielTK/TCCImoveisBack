package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_acesso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "ACAO", nullable = false)
    private String acao;
}
