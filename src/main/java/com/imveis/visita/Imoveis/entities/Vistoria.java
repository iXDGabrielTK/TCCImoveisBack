package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

@Entity
@Table (name = "vistoria")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Vistoria {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idVistoria;

    @Column(name = "TIPO_VISTORIA")
    private String tipoVistoria;

    @Column(name = "LAUDO_VISTORIA")
    private String laudoVistoria;

    @ElementCollection
    @Column(name = "FOTO_VISTORIA")
    private List<String> fotoVistoria;

    @Column(name = "DATA_VISTORIA")
    private Date dataVistoria;


    @ManyToOne
    private Imovel imovel;
}
