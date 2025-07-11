package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long> {

    Optional<Imovel> findByEnderecoImovel_RuaAndEnderecoImovel_NumeroAndEnderecoImovel_Bairro(String rua, String numero, String bairro);

    @Query("SELECT i.idImovel FROM Imovel i WHERE i.apagado = false")
    Page<Long> findAllIdsPaginado(Pageable pageable);

    @Query("SELECT i.idImovel FROM Imovel i WHERE i.apagado = false AND i.precoImovel <= :valorMax")
    Page<Long> findDisponiveisIdsPorValorMax(@Param("valorMax") BigDecimal valorMax, Pageable pageable);

    @Query("""
            SELECT DISTINCT i FROM Imovel i
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.enderecoImovel
            LEFT JOIN FETCH i.imobiliaria
            WHERE i.idImovel IN :ids
            """)
    List<Imovel> findAllByIdInWithFotosAndEndereco(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"corretores", "imobiliaria"})
    Optional<Imovel> findByIdImovel(Long idImovel);

    @EntityGraph(attributePaths = {"fotosImovel"})
    @Query("SELECT i FROM Imovel i WHERE i.idImovel = :id AND i.apagado = false")
    Optional<Imovel> findByIdActive(@Param("id") Long id);

    @Query("""
            SELECT i FROM Imovel i
            LEFT JOIN FETCH i.enderecoImovel
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.imobiliaria
            WHERE i.idImovel = :id AND i.apagado = false
            """)
    Optional<Imovel> findByIdWithEnderecoAndFotos(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT i FROM Imovel i
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.imobiliaria
            WHERE i.imobiliaria.id = :imobiliariaId AND i.apagado = false
            """)
    Page<Imovel> findByImobiliariaId(@Param("imobiliariaId") Long imobiliariaId, Pageable pageable);

    @Query("""
            SELECT DISTINCT i FROM Imovel i
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.enderecoImovel
            LEFT JOIN FETCH i.imobiliaria
            WHERE i.imobiliaria.id IN :imobiliariaIds AND i.apagado = false
            """)
    List<Imovel> findByImobiliariaIds(@Param("imobiliariaIds") List<Long> imobiliariaIds);


}