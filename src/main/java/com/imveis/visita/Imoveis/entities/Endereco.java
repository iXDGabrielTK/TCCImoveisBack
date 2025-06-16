package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "endereco")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEndereco;

    @Column(name = "RUA", nullable = false)
    private String rua;

    @Column(name = "NUMERO", nullable = false)
    private String numero;

    @Column(name = "COMPLEMENTO", nullable = false)
    private String complemento;

    @Column(name = "BAIRRO", nullable = false)
    private String bairro;

    @Column(name = "CIDADE", nullable = false)
    private String cidade;

    @Column(name = "ESTADO", length = 2, nullable = false)
    private String estado;

    @Column(name = "CEP", nullable = false)
    private String cep;
}
