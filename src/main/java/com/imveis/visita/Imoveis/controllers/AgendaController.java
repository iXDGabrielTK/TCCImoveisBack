package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.AgendamentoRequest;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendaController {

    private final AgendamentoService agendamentoService;

    public AgendaController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<Agendamento> agendarVisita(@RequestBody AgendamentoRequest request) {
        try {
            if (request.getUsuarioId() != null) {
                Usuario usuario = new Usuario() {
                };
                usuario.setId(request.getUsuarioId());
                request.setUsuario(usuario);
            }

            Agendamento agendamento = agendamentoService.agendarVisita(request);
            return ResponseEntity.ok(agendamento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable BigInteger id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping(value = "/usuario/{usuarioId}", produces = "application/json")
    public ResponseEntity<List<Agendamento>> getAgendamentosByUsuario(@PathVariable BigInteger usuarioId) {
        try {
            List<Agendamento> agendamentos = agendamentoService.findByUsuarioId(usuarioId);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception e) {
            System.err.println("Erro ao buscar agendamentos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
