package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.repositories.FotoVistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class FotoVistoriaService {

    private final FotoVistoriaRepository fotoVistoriaRepository;

    @Autowired
    public FotoVistoriaService(FotoVistoriaRepository fotoVistoriaRepository) {
        this.fotoVistoriaRepository = fotoVistoriaRepository;
    }

    public List<FotoVistoria> findAll() {
        return fotoVistoriaRepository.findAll();
    }

    public Optional<FotoVistoria> findById(BigInteger id) {
        return fotoVistoriaRepository.findById(id);
    }

    public FotoVistoria save(FotoVistoria fotoVistoria) {
        return fotoVistoriaRepository.save(fotoVistoria);
    }

    public void deleteById(BigInteger id) {
        fotoVistoriaRepository.deleteById(id);
    }
}