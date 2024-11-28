package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.service.RelatorioAgendamentoService;
import com.imveis.visita.Imoveis.service.RelatorioUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.YearMonth;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioUsuarioService relatorioUsuarioService;
    private final RelatorioAgendamentoService relatorioAgendamentoService;

    // Construtor para injetar as dependências
    @Autowired
    public RelatorioController(
            RelatorioUsuarioService relatorioUsuarioService,
            RelatorioAgendamentoService relatorioAgendamentoService
    ) {
        this.relatorioUsuarioService = relatorioUsuarioService;
        this.relatorioAgendamentoService = relatorioAgendamentoService;
    }

    // Endpoint para relatório de usuários
    @GetMapping("/usuarios")
    public ResponseEntity<InputStreamResource> downloadRelatorioUsuarios(@RequestParam YearMonth mesAno) {
        ByteArrayInputStream bis = relatorioUsuarioService.gerarRelatorioUsuarios(mesAno);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_usuarios.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // Endpoint para relatório de agendamentos
    @GetMapping("/agendamentos")
    public ResponseEntity<InputStreamResource> downloadRelatorioAgendamentos(@RequestParam YearMonth mesAno) {
        ByteArrayInputStream bis = relatorioAgendamentoService.gerarRelatorioAgendamentos(mesAno);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_agendamentos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
