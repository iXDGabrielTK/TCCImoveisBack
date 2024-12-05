package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioAgendamentoDTO;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioAgendamentoService {

    @Autowired
    private AgendaRepository agendaRepository;

    // Busca relatório com base no mês e ano (exemplo: "2024-11")
    public List<RelatorioAgendamentoDTO> buscarRelatorioAgendamentos(String mesAno) {
        try {
            // Divide o mesAno (exemplo: "2024-11") em ano e mês
            String[] parts = mesAno.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Formato de data inválido. Use 'AAAA-MM'.");
            }
            int ano = Integer.parseInt(parts[0]);
            int mes = Integer.parseInt(parts[1]);

            // Busca os dados no repositório
            return agendaRepository.buscarRelatorioAgendamentos(ano, mes);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano ou mês inválido no formato fornecido.");
        }
    }

    // Busca relatório baseado apenas no ano
    public List<RelatorioAgendamentoDTO> buscarRelatorioAgendamentosPorAno(int ano) {
        try {
            // Valida o ano
            if (ano < 1000 || ano > 9999) {
                throw new IllegalArgumentException("Ano inválido. Deve estar no formato 'AAAA'.");
            }

            // Busca os dados no repositório
            return agendaRepository.buscarRelatorioAgendamentosPorAno(ano);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Erro ao buscar dados para o ano fornecido: " + e.getMessage());
        }
    }
}
