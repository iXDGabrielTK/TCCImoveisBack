package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.AmbienteVistoria;
import com.imveis.visita.Imoveis.repositories.AmbienteVistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AmbienteVistoriaService {

    private final AmbienteVistoriaRepository repository;

    @Autowired
    public AmbienteVistoriaService(AmbienteVistoriaRepository repository) {
        this.repository = repository;
    }

    public List<AmbienteVistoria> saveAll(List<AmbienteVistoria> ambientes) {
        return repository.saveAll(ambientes);
    }

    public Optional<AmbienteVistoria> findById(Long id) {
        return repository.findById(id);
    }
}
