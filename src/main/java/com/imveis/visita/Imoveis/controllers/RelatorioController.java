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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class RelatorioController {

    @Autowired
    private RelatorioVistoriaService relatorioVistoriaService;

    @Autowired
    private RelatorioUsuarioService relatorioUsuarioService;

    @Autowired
    private RelatorioAgendamentoService relatorioAgendamentoService;

    // Relatório de Vistorias
    @GetMapping("/relatorios/vistorias")
    public ResponseEntity<byte[]> gerarRelatorioVistorias(
            @RequestParam BigInteger idImovel,
            @RequestParam String mesAno) {

        byte[] pdf = relatorioVistoriaService.gerarRelatorioVistorias(idImovel, mesAno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio_vistorias.pdf")
                .body(pdf);
    }

    @GetMapping("/relatorios/vistorias/download")
    public ResponseEntity<InputStreamResource> baixarRelatorioVistorias(
            @RequestParam BigInteger idImovel,
            @RequestParam String mesAno) {

        byte[] pdf = relatorioVistoriaService.gerarRelatorioVistorias(idImovel, mesAno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_vistorias.pdf")
                .body(new InputStreamResource(new java.io.ByteArrayInputStream(pdf)));
    }

    // Relatório de Usuários
    @GetMapping("/relatorios/usuarios")
    public ResponseEntity<byte[]> gerarRelatorioUsuarios(
            @RequestParam(required = false) BigInteger idImovel,
            @RequestParam(required = false) String mesAno) {

        byte[] pdf = relatorioUsuarioService.gerarRelatorioUsuarios(idImovel, mesAno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio_usuarios.pdf")
                .body(pdf);
    }

    @GetMapping("/relatorios/usuarios/download")
    public ResponseEntity<InputStreamResource> baixarRelatorioUsuarios(
            @RequestParam(required = false) BigInteger idImovel,
            @RequestParam(required = false) String mesAno) {

        byte[] pdf = relatorioUsuarioService.gerarRelatorioUsuarios(idImovel, mesAno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_usuarios.pdf")
                .body(new InputStreamResource(new java.io.ByteArrayInputStream(pdf)));
    }

    // Relatório de Agendamentos
    @GetMapping("/relatorios/agendamentos")
    public ResponseEntity<byte[]> gerarRelatorioAgendamentos(
            @RequestParam BigInteger idImovel,
            @RequestParam String mesAno) {

        byte[] pdf = relatorioAgendamentoService.gerarRelatorioAgendamentos(idImovel, mesAno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio_agendamentos.pdf")
                .body(pdf);
    }

    @GetMapping("/relatorios/agendamentos/download")
    public ResponseEntity<InputStreamResource> baixarRelatorioAgendamentos(
            @RequestParam BigInteger idImovel,
            @RequestParam String mesAno) {

        byte[] pdf = relatorioAgendamentoService.gerarRelatorioAgendamentos(idImovel, mesAno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_agendamentos.pdf")
                .body(new InputStreamResource(new java.io.ByteArrayInputStream(pdf)));
    }
}
