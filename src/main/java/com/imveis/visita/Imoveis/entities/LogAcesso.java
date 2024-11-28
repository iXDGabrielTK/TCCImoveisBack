package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_acesso")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    // Relacionamento com Usuário (quem fez o login ou visualizou)
    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    // Data/hora do acesso ou visualização
    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;

    // Tipo de ação (ex.: LOGIN, VISUALIZACAO_IMOVEL)
    @Column(name = "ACAO", nullable = false)
    private String acao;
}
