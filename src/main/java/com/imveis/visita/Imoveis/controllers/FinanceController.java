package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.SimulacaoRequest;
import com.imveis.visita.Imoveis.dtos.SimulacaoResponse;
import com.imveis.visita.Imoveis.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulacoes")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PostMapping
    public ResponseEntity<SimulacaoResponse> simular(@RequestBody SimulacaoRequest request) {
        SimulacaoResponse response = financeService.simularFinanciamento(request);
        return ResponseEntity.ok(response);
    }
}
