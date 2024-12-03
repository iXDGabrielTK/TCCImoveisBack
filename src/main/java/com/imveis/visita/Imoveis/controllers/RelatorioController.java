package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.RelatorioAgendamentoDTO;
import com.imveis.visita.Imoveis.dtos.RelatorioUsuarioDTO;
import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO;
import com.imveis.visita.Imoveis.service.RelatorioAgendamentoService;
import com.imveis.visita.Imoveis.service.RelatorioUsuarioService;
import com.imveis.visita.Imoveis.service.RelatorioVistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // Relatório de Vistorias
    @GetMapping("/vistorias")
    public ResponseEntity<List<RelatorioVistoriaDTO>> getRelatorioVistorias(@RequestParam BigInteger idImovel) {
        List<RelatorioVistoriaDTO> relatorio = relatorioVistoriaService.buscarRelatorioVistorias(idImovel);
        return ResponseEntity.ok(relatorio);
    }

    // Relatório de Usuários
    @GetMapping("/usuarios")
    public ResponseEntity<List<RelatorioUsuarioDTO>> getRelatorioUsuarios(@RequestParam String mesAno) {
        List<RelatorioUsuarioDTO> relatorio = relatorioUsuarioService.buscarRelatorioUsuarios(mesAno);
        return ResponseEntity.ok(relatorio);
    }




    // Relatório de Agendamentos
    @GetMapping("/agendamentos")
    public ResponseEntity<List<RelatorioAgendamentoDTO>> getRelatorioAgendamentos(@RequestParam String mesAno) {
        List<RelatorioAgendamentoDTO> relatorio = relatorioAgendamentoService.buscarRelatorioAgendamentos(mesAno);
        return ResponseEntity.ok(relatorio);
    }
}