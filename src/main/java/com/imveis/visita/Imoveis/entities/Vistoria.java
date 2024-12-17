package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "vistoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idVistoria")
public class Vistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private BigInteger idVistoria;

    @Column(name = "tipo_vistoria")
    private String tipoVistoria;

    @Column(name = "laudo_vistoria", columnDefinition = "TEXT")
    private String laudoVistoria;

    @Column(name = "DATA_VISTORIA")
    private LocalDate dataVistoria;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false, referencedColumnName = "id")
    private Imovel imovel;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(name = "apagado", nullable = false)
    private boolean apagado = false;

}