package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.AgendamentoRequest;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.service.AgendamentoService;
import org.springframework.http.MediaType;
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



    @GetMapping(value = "/usuario/{usuarioid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Agendamento>> listarAgendamentosPorUsuario(@PathVariable BigInteger usuarioid) {
        System.out.println("Endpoint '/agendamentos/usuario/{usuarioId}' chamado com ID: " + usuarioid);
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorUsuario(usuarioid);
        return ResponseEntity.ok(agendamentos);
    }


}
