package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.dtos.RelatorioFavoritoDTO;
import com.imveis.visita.Imoveis.entities.Favorito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    @Query("SELECT f FROM Favorito f WHERE f.usuario.id = :usuarioId AND f.imovel.idImovel = :imovelId")
    Optional<Favorito> findByUsuarioIdAndImovelId(@Param("usuarioId") Long usuarioId, @Param("imovelId") Long imovelId);

    @EntityGraph(attributePaths = {"imovel.enderecoImovel", "imovel.fotosImovel"})
    List<Favorito> findAllByUsuarioId(Long usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorito f WHERE f.usuario.id = :usuarioId AND f.imovel.idImovel = :imovelId")
    void deleteByUsuarioIdAndImovelId(@Param("usuarioId") Long usuarioId, @Param("imovelId") Long imovelId);

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioFavoritoDTO(" +
            "f.imovel.idImovel, " +
            "f.imovel.descricaoImovel, " +
            "COUNT(f.id)) " +
            "FROM Favorito f " +
            "WHERE EXTRACT(YEAR FROM f.dataFavorito) = :ano AND EXTRACT(MONTH FROM f.dataFavorito) = :mes " +
            "GROUP BY f.imovel.idImovel, f.imovel.descricaoImovel " +
            "ORDER BY COUNT(f.id) DESC")
    List<RelatorioFavoritoDTO> countFavoritosByImovelAndMonthYear(@Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioFavoritoDTO(" +
            "f.imovel.idImovel, " +
            "f.imovel.descricaoImovel, " +
            "COUNT(f.id)) " +
            "FROM Favorito f " +
            "WHERE EXTRACT(YEAR FROM f.dataFavorito) = :ano " +
            "GROUP BY f.imovel.idImovel, f.imovel.descricaoImovel " +
            "ORDER BY COUNT(f.id) DESC")
    List<RelatorioFavoritoDTO> countFavoritosByImovelAndYear(@Param("ano") int ano);
}