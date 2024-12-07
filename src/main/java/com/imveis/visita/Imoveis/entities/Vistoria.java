package com.imveis.visita.Imoveis.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

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

    @Column(name = "laudo_vistoria", columnDefinition = "TEXT")
    private String laudoVistoria;

    @Column(name = "DATA_VISTORIA")
    private Date dataVistoria;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false, referencedColumnName = "id")
    private Imovel imovel;

    // Adicionar relacionamento com fotos
    @OneToMany(mappedBy = "vistoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FotoVistoria> fotosvistoria;
}