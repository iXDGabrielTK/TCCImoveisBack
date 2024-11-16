package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    private final AgendaRepository agendaRepository;

    public AgendamentoService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public long countAgendamentosByMonth(int ano, int mes) {
        return agendaRepository.countAgendamentosByMonth(ano, mes);
    }

    public Map<BigInteger, Long> countAgendamentosByImovelAndMonth(int ano, int mes) {
        List<Object[]> results = agendaRepository.countAgendamentosByImovelAndMonth(ano, mes);
        return results.stream()
                .collect(Collectors.toMap(
                        r -> (BigInteger) r[0], // ID do imóvel
                        r -> (Long) r[1]        // Total de agendamentos
                ));
    }
}