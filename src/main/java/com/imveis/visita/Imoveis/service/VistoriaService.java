package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VistoriaService {

    private final VistoriaRepository vistoriaRepository;
    private final CorretorRepository corretorRepository;

    @Autowired
    public VistoriaService(VistoriaRepository vistoriaRepository, CorretorRepository corretorRepository) {
        this.vistoriaRepository = vistoriaRepository;
        this.corretorRepository = corretorRepository;
    }

    @Transactional(readOnly = true) // Garante que a sessão esteja aberta durante a conversão para DTOs
    public List<Vistoria> findAll() {
        return vistoriaRepository.findAllActiveWithAllDetails();
    }

    @Transactional(readOnly = true) // Garante que a sessão esteja aberta
    public Optional<Vistoria> findById(Long id) {
        return vistoriaRepository.findByIdActiveWithAllDetails(id);
    }

    public Vistoria save(Vistoria vistoria) {
        return vistoriaRepository.save(vistoria);
    }

    public void cancelarVistoria(Long id) {
        Vistoria vistoria = vistoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma Vistoria encontrado com o ID especificado."));

        vistoria.setApagado(true);
        vistoriaRepository.save(vistoria);
    }

    public void deleteById(Long id) {
        vistoriaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Vistoria> findAllByCorretorAfiliado(Long corretorId) {
        Corretor corretor = corretorRepository.findById(corretorId)
                .orElseThrow(() -> new IllegalArgumentException("Corretor não encontrado."));
        List<Long> imobiliariaIds = corretor.getImobiliarias().stream()
                .map(imob -> imob.getId())
                .collect(Collectors.toList());
        if (imobiliariaIds.isEmpty()) {
            return List.of();
        }
        return vistoriaRepository.findAllByImobiliariaIds(imobiliariaIds);
    }

}