package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.service.RelatorioAgendamentoService;
import com.imveis.visita.Imoveis.service.RelatorioUsuarioService;
import com.imveis.visita.Imoveis.service.RelatorioVistoriaService;
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
import java.math.BigInteger;
import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioUsuarioService relatorioUsuarioService;

    @Autowired
    private RelatorioAgendamentoService relatorioAgendamentoService;

    @Autowired
    private RelatorioVistoriaService relatorioVistoriaService;

    // Relatório de Usuários
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

    // Relatório de Agendamentos
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

    // Relatório de Vistorias
    @GetMapping("/vistorias")
    public ResponseEntity<InputStreamResource> downloadRelatorioVistorias(@RequestParam BigInteger idImovel) {
        ByteArrayInputStream bis = relatorioVistoriaService.gerarRelatorioVistorias(idImovel);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_vistorias.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
