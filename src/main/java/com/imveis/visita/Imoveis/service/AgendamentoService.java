package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendaRepository agendaRepository;

    public AgendamentoService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public void agendarVisita(String nomeVisitante, Imovel imovel, Date dataAgendamento, Boolean horarioMarcado) {
        if (agendaRepository.existsByImovelAndDataAgendamentoAndHorarioMarcado(imovel, dataAgendamento, horarioMarcado)) {
            throw new IllegalArgumentException("Horário já ocupado para esse imóvel.");
        }
        Agendamento agendamento = new Agendamento();
        agendamento.setNomeVisitante(nomeVisitante);
        agendamento.setImovel(imovel);
        agendamento.setDataAgendamento(dataAgendamento);
        agendamento.setHorarioMarcado(horarioMarcado);

        agendaRepository.save(agendamento);
    }

    public List<Agendamento> listarAgendamentos(BigInteger idImovel) {
        return agendaRepository.findByImovelId(idImovel);
    }
}
