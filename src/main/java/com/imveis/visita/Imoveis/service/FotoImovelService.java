package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.repositories.FotoImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FotoImovelService {

    private final FotoImovelRepository fotoImovelRepository;

    @Autowired
    public FotoImovelService(FotoImovelRepository fotoImovelRepository) {
        this.fotoImovelRepository = fotoImovelRepository;
    }

    public List<FotoImovel> findAll() {
        return fotoImovelRepository.findAll();
    }

    public Optional<FotoImovel> findById(Long id) {
        return fotoImovelRepository.findById(id);
    }

    public FotoImovel save(FotoImovel fotoImovel) {
        return fotoImovelRepository.save(fotoImovel);
    }

    public void deleteById(Long id) {
        fotoImovelRepository.deleteById(id);
    }

}
