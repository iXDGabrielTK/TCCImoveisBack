package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.SimulacaoRequest;
import com.imveis.visita.Imoveis.dtos.SimulacaoResponse;
import com.imveis.visita.Imoveis.service.FinanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulacoes")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PostMapping
    public ResponseEntity<SimulacaoResponse> simular(@RequestBody SimulacaoRequest request, HttpServletRequest req) {
        try {
            if (request.getRendaMensal() == null || request.getValorEntrada() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            System.out.println("🔐 Header Authorization: " + req.getHeader("Authorization"));
            SimulacaoResponse response = financeService.simularFinanciamento(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SimulacaoResponse("Erro: " + e.getMessage()));
        }

    }
}
