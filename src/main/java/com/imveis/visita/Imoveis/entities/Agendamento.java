package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "DATA_AGENDAMENTO", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "NOME_VISITANTE", nullable = false)
    private String nomeVisitante;

    @Column(name = "HORARIO_MARCADO", nullable = false)
    private Boolean horarioMarcado;

    @ManyToOne
    @JoinColumn(name = "IMOVEL_ID")
    @JsonBackReference
    private Imovel imovel;
}
