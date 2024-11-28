package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Usuario;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class AgendamentoRequest {
    private BigInteger imovelId;
    private String nomeVisitante;
    private LocalDate dataAgendamento;
    private boolean horarioMarcado;

    // ALTERAÇÃO: Adicionando o campo usuarioId
    private BigInteger usuarioId;

    public void setUsuario(Usuario usuario) {
    }
}
