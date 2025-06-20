package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioFavoritoDTO;
import com.imveis.visita.Imoveis.repositories.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioFavoritoService {

    private final FavoritoRepository favoritoRepository;

    @Autowired
    public RelatorioFavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    public List<RelatorioFavoritoDTO> buscarRelatorioFavoritos(String mesAno) {
        if (mesAno == null || mesAno.isEmpty()) {
            throw new IllegalArgumentException("O parâmetro 'mesAno' não pode ser nulo ou vazio.");
        }
        String[] parts = mesAno.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato de 'mesAno' inválido. Use AAAA-MM.");
        }
        try {
            int ano = Integer.parseInt(parts[0]);
            int mes = Integer.parseInt(parts[1]);
            return favoritoRepository.countFavoritosByImovelAndMonthYear(ano, mes);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano ou mês inválido no parâmetro 'mesAno'.", e);
        }
    }

    public List<RelatorioFavoritoDTO> buscarRelatorioFavoritosPorAno(Integer ano) {
        if (ano == null) {
            throw new IllegalArgumentException("O parâmetro 'ano' não pode ser nulo.");
        }
        return favoritoRepository.countFavoritosByImovelAndYear(ano);
    }
}