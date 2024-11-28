package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.AgendamentoRequest;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            // ALTERAÇÃO: Associar usuário se usuarioId for fornecido
            if (request.getUsuarioId() != null) {
                Usuario usuario = new Usuario() {
                    // Classe anônima já que Usuario é abstrata
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

    @DeleteMapping("/cancelar")
    public ResponseEntity<String> cancelarAgendamento(
            @RequestParam BigInteger imovelId,
            @RequestParam String data, // Recebe a data como string
            @RequestParam boolean horarioMarcado) {
        try {
            LocalDate dataAgendamento = LocalDate.parse(data);
            agendamentoService.cancelarAgendamento(imovelId, dataAgendamento, horarioMarcado);
            return ResponseEntity.ok("Agendamento cancelado com sucesso!");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de data inválido.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar agendamento.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentosPorImovel(@RequestParam BigInteger imovelId) {
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorImovel(imovelId);
        return ResponseEntity.ok(agendamentos);
    }
}
