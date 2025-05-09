package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.RelatorioAgendamentoDTO;
import com.imveis.visita.Imoveis.dtos.RelatorioUsuarioDTO;
import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO;
import com.imveis.visita.Imoveis.service.RelatorioAgendamentoService;
import com.imveis.visita.Imoveis.service.RelatorioUsuarioService;
import com.imveis.visita.Imoveis.service.RelatorioVistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioVistoriaService relatorioVistoriaService;

    @Autowired
    private RelatorioUsuarioService relatorioUsuarioService;

    @Autowired
    private RelatorioAgendamentoService relatorioAgendamentoService;

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/vistorias")
    public ResponseEntity<?> gerarRelatorioVistorias(@RequestParam BigInteger idImovel) {
        try {
            List<RelatorioVistoriaDTO> relatorio = relatorioVistoriaService.buscarRelatorioVistorias(idImovel);
            return ResponseEntity.ok(relatorio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/usuarios")
    public ResponseEntity<?> gerarRelatorioUsuarios(
            @RequestParam(required = false) String mesAno,
            @RequestParam(required = false) Integer ano) {
        try {
            if (mesAno != null) {
                List<RelatorioUsuarioDTO> relatorio = relatorioUsuarioService.buscarRelatorioUsuarios(mesAno);
                return ResponseEntity.ok(relatorio);
            }

            if (ano != null) {
                List<RelatorioUsuarioDTO> relatorio = relatorioUsuarioService.buscarRelatorioUsuariosPorAno(ano);
                return ResponseEntity.ok(relatorio);
            }

            return ResponseEntity.badRequest().body("Por favor, forneça o parâmetro 'mesAno' ou 'ano'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/agendamentos")
    public ResponseEntity<?> getRelatorioAgendamentos(
            @RequestParam(required = false) String mesAno,
            @RequestParam(required = false) Integer ano) {
        try {
            if (mesAno != null) {
                List<RelatorioAgendamentoDTO> relatorio = relatorioAgendamentoService.buscarRelatorioAgendamentos(mesAno);
                return ResponseEntity.ok(relatorio);
            }

            if (ano != null) {
                List<RelatorioAgendamentoDTO> relatorio = relatorioAgendamentoService.buscarRelatorioAgendamentosPorAno(ano);
                return ResponseEntity.ok(relatorio);
            }

            return ResponseEntity.badRequest().body("Por favor, forneça o parâmetro 'mesAno' ou 'ano'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
