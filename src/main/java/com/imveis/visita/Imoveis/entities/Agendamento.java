package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "agendamento")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column(name = "CANCELADO", nullable = false)
    private boolean cancelado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID")
    @JsonBackReference
    private Usuario usuario;
}
