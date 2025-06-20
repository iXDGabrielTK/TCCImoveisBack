package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO; // Para a lista do select
import com.imveis.visita.Imoveis.dtos.VistoriaDTO; // Para os detalhes da vistoria
import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe o @Transactional

import java.util.List;
import java.util.Optional; // Importe Optional

@Service
public class RelatorioVistoriaService {

    @Autowired
    private VistoriaRepository vistoriaRepository;

    @Transactional(readOnly = true)
    public VistoriaDTO buscarVistoriaDetalhadaPorId(Long idVistoria) {
        Optional<Vistoria> vistoriaOpt = vistoriaRepository.findByIdActiveWithAllDetails(idVistoria);
        return vistoriaOpt.map(VistoriaDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Vistoria com ID " + idVistoria + " não encontrada ou está inativa."));
    }

    @Transactional(readOnly = true)
    public List<RelatorioVistoriaDTO> buscarTodasVistoriasParaSelecao() {
        return vistoriaRepository.findAllActiveVistoriasForSelection();
    }


}