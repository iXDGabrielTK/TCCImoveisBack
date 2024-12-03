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
        // Divide o mesAno (exemplo: "2024-11") em ano e mês
        String[] parts = mesAno.split("-");
        int ano = Integer.parseInt(parts[0]);
        int mes = Integer.parseInt(parts[1]);

        // Busca os dados no repositório
        return logAcessoRepository.buscarRelatorioUsuarios(ano, mes);
    }
}
