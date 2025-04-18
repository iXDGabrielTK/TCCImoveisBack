package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioUsuarioDTO;
import com.imveis.visita.Imoveis.repositories.LogAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioUsuarioService {

    @Autowired
    private LogAcessoRepository logAcessoRepository;

    public List<RelatorioUsuarioDTO> buscarRelatorioUsuarios(String mesAno) {
        try {
            String[] parts = mesAno.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Formato de data inválido. Use 'AAAA-MM'.");
            }
            int ano = Integer.parseInt(parts[0]);
            int mes = Integer.parseInt(parts[1]);

            return logAcessoRepository.buscarRelatorioUsuarios(ano, mes);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano ou mês inválido no formato fornecido.");
        }
    }

    public List<RelatorioUsuarioDTO> buscarRelatorioUsuariosPorAno(int ano) {
        try {
            if (ano < 1000 || ano > 9999) {
                throw new IllegalArgumentException("Ano inválido. Deve estar no formato 'AAAA'.");
            }

            return logAcessoRepository.buscarRelatorioUsuariosPorAno(ano);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Erro ao buscar dados para o ano fornecido: " + e.getMessage());
        }
    }
}
