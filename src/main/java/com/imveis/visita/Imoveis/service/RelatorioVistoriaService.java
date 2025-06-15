package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioVistoriaService {

    @Autowired
    private VistoriaRepository vistoriaRepository;

    public List<RelatorioVistoriaDTO> buscarRelatorioVistorias(Long idImovel) {
        return vistoriaRepository.buscarRelatorioVistorias(idImovel);
    }
}