package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.*;
import com.imveis.visita.Imoveis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable; // Importe PathVariable

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
    @Autowired
    private RelatorioPropostaService relatorioPropostaService;
    @Autowired // Injete o novo serviço
    private RelatorioFavoritoService relatorioFavoritoService;

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/vistorias/{idVistoria}") // Endpoint para buscar UMA vistoria detalhada por ID da vistoria
    public ResponseEntity<?> gerarRelatorioVistoriaDetalhada(@PathVariable Long idVistoria) {
        try {
            VistoriaDTO relatorio = relatorioVistoriaService.buscarVistoriaDetalhadaPorId(idVistoria);
            return ResponseEntity.ok(relatorio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/vistorias/lista-para-selecao") // Novo endpoint para listar vistorias para o select
    public ResponseEntity<List<RelatorioVistoriaDTO>> listarVistoriasParaSelecao() {
        List<RelatorioVistoriaDTO> vistorias = relatorioVistoriaService.buscarTodasVistoriasParaSelecao();
        return ResponseEntity.ok(vistorias);
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

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/propostas") // Novo endpoint para relatório de propostas
    public ResponseEntity<?> gerarRelatorioPropostas(
            @RequestParam(required = false) String mesAno,
            @RequestParam(required = false) Integer ano) {
        try {
            if (mesAno != null) {
                List<RelatorioPropostaDTO> relatorio = relatorioPropostaService.buscarRelatorioPropostas(mesAno);
                return ResponseEntity.ok(relatorio);
            }

            if (ano != null) {
                List<RelatorioPropostaDTO> relatorio = relatorioPropostaService.buscarRelatorioPropostasPorAno(ano);
                return ResponseEntity.ok(relatorio);
            }

            return ResponseEntity.badRequest().body("Por favor, forneça o parâmetro 'mesAno' ou 'ano'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @GetMapping("/favoritos")
    public ResponseEntity<?> gerarRelatorioFavoritos(
            @RequestParam(required = false) String mesAno,
            @RequestParam(required = false) Integer ano) {
        try {
            if (mesAno != null && !mesAno.isEmpty()) {
                List<RelatorioFavoritoDTO> relatorio = relatorioFavoritoService.buscarRelatorioFavoritos(mesAno);
                return ResponseEntity.ok(relatorio);
            }

            if (ano != null) {
                List<RelatorioFavoritoDTO> relatorio = relatorioFavoritoService.buscarRelatorioFavoritosPorAno(ano);
                return ResponseEntity.ok(relatorio);
            }

            return ResponseEntity.badRequest().body("Por favor, forneça o parâmetro 'mesAno' ou 'ano'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}