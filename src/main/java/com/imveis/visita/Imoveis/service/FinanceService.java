package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.SimulacaoRequest;
import com.imveis.visita.Imoveis.dtos.SimulacaoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FinanceService {

    private static final BigDecimal PERCENTUAL_RENDA = new BigDecimal("0.30");
    private static final int PRAZO_MESES = 360;

    public SimulacaoResponse simularFinanciamento(SimulacaoRequest request) {
        BigDecimal rendaMensal = request.getRendaMensal();
        BigDecimal entrada = request.getValorEntrada();

        if(rendaMensal == null || entrada == null || rendaMensal.compareTo(BigDecimal.ZERO) <=  0) {
            throw new IllegalArgumentException("Renda mensal e entrada devem ser válidos");
        }

        BigDecimal valorMaxParcela = rendaMensal.multiply(PERCENTUAL_RENDA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinanciamento = valorMaxParcela.multiply(BigDecimal.valueOf(PRAZO_MESES)).setScale(2,RoundingMode.HALF_UP);
        BigDecimal poderDeCompra = entrada.add(valorFinanciamento).setScale(2, RoundingMode.HALF_UP);

        return new SimulacaoResponse(valorMaxParcela, valorFinanciamento, poderDeCompra);
    }
}
