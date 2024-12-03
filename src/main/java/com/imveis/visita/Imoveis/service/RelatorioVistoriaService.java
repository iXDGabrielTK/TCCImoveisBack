package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioVistoriaService {

    @Autowired
    private VistoriaRepository vistoriaRepository;

    public List<RelatorioVistoriaDTO> buscarRelatorioVistorias(BigInteger idImovel) {
        // Busca os dados no repositório
        return vistoriaRepository.buscarRelatorioVistorias(idImovel);
    }
}
