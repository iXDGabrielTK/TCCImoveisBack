package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.SimulacaoRequest;
import com.imveis.visita.Imoveis.dtos.SimulacaoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class FinanceService {

    private static final BigDecimal PERCENTUAL_RENDA = new BigDecimal("0.30");
    private static final List<Integer> PRAZO_MESES_PERMITIDOS = List.of(120,180,240,300,360);

    public SimulacaoResponse simularFinanciamento(SimulacaoRequest request) {
        BigDecimal rendaMensal = request.getRendaMensal();
        BigDecimal entrada = request.getValorEntrada();
        int prazo = request.getPrazo();

        if(rendaMensal == null || entrada == null || rendaMensal.compareTo(BigDecimal.ZERO) <=  0) {
            throw new IllegalArgumentException("Renda mensal e entrada devem ser válidos");
        }

        if(!PRAZO_MESES_PERMITIDOS.contains(prazo)){
            throw new IllegalArgumentException("Prazo inválido. Os prazos permitidos são: " + PRAZO_MESES_PERMITIDOS);
        }

        BigDecimal valorMaxParcela = rendaMensal.multiply(PERCENTUAL_RENDA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinanciamento = valorMaxParcela.multiply(BigDecimal.valueOf(prazo)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal poderDeCompra = entrada.add(valorFinanciamento).setScale(2, RoundingMode.HALF_UP);

        return new SimulacaoResponse(valorMaxParcela, valorFinanciamento, poderDeCompra);
    }
}
