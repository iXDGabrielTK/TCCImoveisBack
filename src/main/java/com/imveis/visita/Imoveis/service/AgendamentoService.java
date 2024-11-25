package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.AgendamentoRequest;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    private final AgendaRepository agendaRepository;
    private final ImovelRepository imovelRepository;

    public AgendamentoService(AgendaRepository agendaRepository, ImovelRepository imovelRepository) {
        this.agendaRepository = agendaRepository;
        this.imovelRepository = imovelRepository;
    }

    public Agendamento agendarVisita(AgendamentoRequest request) {
        // Valida se o imóvel existe
        Optional<Imovel> imovelOptional = imovelRepository.findById(request.getImovelId());
        if (imovelOptional.isEmpty()) {
            throw new IllegalArgumentException("Imóvel com ID " + request.getImovelId() + " não encontrado.");
        }

        // Valida se já existe agendamento para o mesmo dia e período
        boolean agendamentoExistente = agendaRepository.existsByImovelIdAndDataAgendamentoAndHorarioMarcado(
                request.getImovelId(),
                request.getDataAgendamento(),
                request.isHorarioMarcado()
        );

        if (agendamentoExistente) {
            throw new IllegalArgumentException("Já existe um agendamento para este imóvel no mesmo dia e período.");
        }

        // Cria o agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setImovel(imovelOptional.get());
        agendamento.setNomeVisitante(request.getNomeVisitante());
        agendamento.setDataAgendamento(request.getDataAgendamento());
        agendamento.setHorarioMarcado(request.isHorarioMarcado());

        return agendaRepository.save(agendamento);
    }

    public void cancelarAgendamento(BigInteger imovelId, LocalDate data, boolean horarioMarcado) {
        Optional<Agendamento> agendamento = agendaRepository.findByImovelIdAndDataAgendamentoAndHorarioMarcado(imovelId, data, horarioMarcado);

        if (agendamento.isEmpty()) {
            throw new IllegalArgumentException("Nenhum agendamento encontrado para o imóvel na data especificada.");
        }

        agendaRepository.delete(agendamento.get());
    }

    public List<Agendamento> buscarAgendamentosPorImovel(BigInteger imovelId) {
        return agendaRepository.findByImovelId(imovelId);
    }




}

