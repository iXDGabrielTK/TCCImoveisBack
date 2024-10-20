package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ManyToAny;

import java.math.BigInteger;
import java.util.List;

@Entity(name = "FotosImovel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class FotoImovel {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger idFotosImovel;

    @Column(name = "IMOVEL_ID")
    @ManyToOne
    private Imovel imovel;

   /* @Column(name = "URL_FOTO_IMOVEL")
    @ManyToAny
    private List<String> urlFotoImovel;
    */

   @ElementCollection // Se for apenas uma lista de URLs
    private List<String> urlFotoImovel;

}
