package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Favorito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface    FavoritoRepository extends JpaRepository<Favorito, Long> {

    @EntityGraph(attributePaths = {
            "imovel.enderecoImovel",
            "imovel.fotosImovel",
            "imovel.corretores",
            "imovel.imobiliarias"
    })
    List<Favorito> findByVisitanteId(Long visitanteId);


    boolean existsByVisitanteIdAndImovel_IdImovel(Long visitanteId, Long imovelId);

    void deleteByVisitanteIdAndImovel_IdImovel(Long visitanteId, Long imovelId);
}


