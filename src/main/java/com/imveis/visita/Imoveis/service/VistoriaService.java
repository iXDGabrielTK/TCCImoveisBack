package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class VistoriaService {

    private final VistoriaRepository vistoriaRepository;

    @Autowired
    public VistoriaService(VistoriaRepository vistoriaRepository) {
        this.vistoriaRepository = vistoriaRepository;
    }

    public List<Vistoria> findAll() {
        return vistoriaRepository.findAllActive();
    }

    public Optional<Vistoria> findById(BigInteger id) {
        return vistoriaRepository.findByIdActive(id);
    }

    public Vistoria save(Vistoria vistoria) {
        return vistoriaRepository.save(vistoria);
    }

    public void cancelarVistoria(BigInteger id) {
        Vistoria vistoria = vistoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma Vistoria encontrado com o ID especificado."));

        vistoria.setApagado(true);
        vistoriaRepository.save(vistoria);
    }

    public void deleteById(BigInteger id) {
        vistoriaRepository.deleteById(id);
    }


}
