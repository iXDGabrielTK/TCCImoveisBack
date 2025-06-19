package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.ImovelDTO;
import com.imveis.visita.Imoveis.entities.Favorito;
import com.imveis.visita.Imoveis.repositories.FavoritoRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.VisitanteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ImovelRepository imovelRepository;
    private final VisitanteRepository visitanteRepository;

    public FavoritoService(FavoritoRepository favoritoRepository,
                           ImovelRepository imovelRepository,
                           VisitanteRepository visitanteRepository) {
        this.favoritoRepository = favoritoRepository;
        this.imovelRepository = imovelRepository;
        this.visitanteRepository = visitanteRepository;
    }

    public void adicionarFavorito(Long idVisitante, Long idImovel) {
        if (favoritoRepository.existsByVisitanteIdAndImovel_IdImovel(idVisitante, idImovel)) {
            throw new IllegalStateException("Imóvel já favoritado.");
        }

        var visitante = visitanteRepository.findById(idVisitante)
                .orElseThrow(() -> new IllegalArgumentException("Visitante não encontrado"));
        var imovel = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado"));

        var favorito = new Favorito();
        favorito.setVisitante(visitante);
        favorito.setImovel(imovel);
        favoritoRepository.save(favorito);
    }

    public void removerFavorito(Long idVisitante, Long idImovel) {
        favoritoRepository.deleteByVisitanteIdAndImovel_IdImovel(idVisitante, idImovel);
    }

    public List<ImovelDTO> listarFavoritos(Long idVisitante) {
        return favoritoRepository.findByVisitanteId(idVisitante).stream()
                .map(f -> {
                    if (f.getImovel() == null) {
                        throw new IllegalStateException("Favorito sem imóvel associado");
                    }
                    return new ImovelDTO(f.getImovel());
                })
                .toList();
    }
}

