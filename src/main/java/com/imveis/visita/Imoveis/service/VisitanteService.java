package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.repositories.VisitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;

    @Autowired
    public VisitanteService(VisitanteRepository visitanteRepository) {
        this.visitanteRepository = visitanteRepository;
    }

    public List<Visitante> findAll() {
        return visitanteRepository.findAll();
    }

    public Optional<Visitante> findById(Long id) {
        return visitanteRepository.findById(id);
    }


    public Visitante save(Visitante visitante) {
        if (visitante.getDataCadastro() == null) {
            visitante.setDataCadastro(LocalDateTime.now());
        }
        return visitanteRepository.save(visitante);
    }


    public void deleteById(Long id) {
        visitanteRepository.deleteById(id);
    }
}
