package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "propostas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal rendaMensal;

    private BigDecimal entrada;

    private BigDecimal valorImovel;

    private BigDecimal valorFinanciamento;

    private LocalDate dataProposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
