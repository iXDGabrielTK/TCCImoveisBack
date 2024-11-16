package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Agendamento {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "DATA_AGENDAMENTO", nullable = false)
    private LocalDateTime dataAgendamento;

    @Column(name = "NOME_VISITANTE", nullable = false)
    private String nomeVisitante;

    @ManyToOne
    @JoinColumn(name = "imovel_id")
    @JsonBackReference
    private Imovel imovel;


}

