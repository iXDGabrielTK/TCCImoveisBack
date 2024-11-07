package com.imveis.visita.Imoveis.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "visitante")
@Getter
@Setter
public class Visitante extends Usuario {
}
