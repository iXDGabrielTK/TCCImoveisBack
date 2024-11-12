package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;

@Entity
@Table (name = "agendamento")
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

    @Column(name = "DATA_AGENDAMENTO")
    private Date dataAgendamento;

    @Column(name = "NOME_VISITANTE")
    private String nomeVisitante;

    @Column(name = "HORARIO")
    private Boolean horarioMarcado;

    @JoinColumn(name = "IMOVEL_ID")
    @ManyToOne
    private Imovel imovel;

}
