package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.AgendamentoRequest;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendaController {

    private final ImovelRepository imovelRepository;
    private final AgendamentoService agendamentoService;

    public AgendaController(ImovelRepository imovelRepository, AgendamentoService agendamentoService) {
        this.imovelRepository = imovelRepository;
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<String> agendarVisita(@RequestBody AgendamentoRequest request) {
        try {
            Imovel imovel = imovelRepository.findById(request.getImovelId())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado."));
            agendamentoService.agendarVisita(request.getNomeVisitante(), imovel, request.getDataAgendamento());
            return ResponseEntity.ok("Agendado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar/{imovelId}")
    public ResponseEntity<List<Agendamento>> listarAgendamento(@PathVariable BigInteger imovelId) {
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentos(imovelId);
        return ResponseEntity.ok(agendamentos);
    }
}
