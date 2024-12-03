package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.AgendamentoRequest;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendaRepository agendaRepository;
    private final ImovelRepository imovelRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendamentoService(AgendaRepository agendaRepository, ImovelRepository imovelRepository, UsuarioRepository usuarioRepository) {
        this.agendaRepository = agendaRepository;
        this.imovelRepository = imovelRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Agendamento agendarVisita(AgendamentoRequest request) {
        Imovel imovel = validarImovel(request.getImovelId());
        validarAgendamentoExistente(request);

        Agendamento agendamento = criarNovoAgendamento(request, imovel);

        return agendaRepository.save(agendamento);
    }

    private Imovel validarImovel(BigInteger imovelId) {
        return imovelRepository.findById(imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel com ID " + imovelId + " não encontrado."));
    }

    private void validarAgendamentoExistente(AgendamentoRequest request) {
        boolean agendamentoExistente = agendaRepository.existsByImovelIdAndDataAgendamentoAndHorarioMarcado(
                request.getImovelId(),
                request.getDataAgendamento(),
                request.isHorarioMarcado()
        );

        if (agendamentoExistente) {
            throw new IllegalArgumentException("Já existe um agendamento para este imóvel no mesmo dia e período.");
        }
    }

    private Agendamento criarNovoAgendamento(AgendamentoRequest request, Imovel imovel) {
        Agendamento agendamento = new Agendamento();
        agendamento.setImovel(imovel);
        agendamento.setNomeVisitante(request.getNomeVisitante());
        agendamento.setDataAgendamento(request.getDataAgendamento());
        agendamento.setHorarioMarcado(request.isHorarioMarcado());

        if (request.getUsuario_Id() != null) {
            Usuario usuario = usuarioRepository.findById(request.getUsuario_Id())
                    .orElseThrow(() -> new IllegalArgumentException("Visitante com ID " + request.getUsuario_Id() + " não encontrado."));
            agendamento.setUsuario(usuario);
        }

        return agendamento;
    }

    public void cancelarAgendamento(BigInteger id) {
        Agendamento agendamento = agendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum agendamento encontrado com o ID especificado."));

        agendamento.setCancelado(true);
        agendaRepository.save(agendamento);
    }

    public List<Agendamento> findByUsuarioId(BigInteger usuarioId) {
        return agendaRepository.findByUsuarioId(usuarioId);
    }
}
