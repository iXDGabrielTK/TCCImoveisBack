package com.imveis.visita.Imoveis.repositories;

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
}
