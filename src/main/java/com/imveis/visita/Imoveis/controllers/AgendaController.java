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
import java.util.Map;

@RestController
@RequestMapping("/agendamentos")
public class AgendaController {

    private final AgendamentoService agendamentoService;

    public AgendaController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("/relatorio/mes")
    public ResponseEntity<Long> relatorioMensal(@RequestParam int ano, @RequestParam int mes) {
        long totalAgendamentos = agendamentoService.countAgendamentosByMonth(ano, mes);
        return ResponseEntity.ok(totalAgendamentos);
    }

    @GetMapping("/relatorio/mes/imoveis")
    public ResponseEntity<Map<BigInteger, Long>> relatorioMensalPorImovel(@RequestParam int ano, @RequestParam int mes) {
        Map<BigInteger, Long> agendamentosPorImovel = agendamentoService.countAgendamentosByImovelAndMonth(ano, mes);
        return ResponseEntity.ok(agendamentosPorImovel);
    }
}

