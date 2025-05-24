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
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ImovelRepository extends JpaRepository<Imovel, BigInteger> {

    Optional<Imovel> findByEnderecoImovel_RuaAndEnderecoImovel_NumeroAndEnderecoImovel_Bairro(String rua, String numero, String bairro);

    // Etapa 1: Buscar apenas os IDs com paginação real
    @Query("SELECT i.idImovel FROM Imovel i WHERE i.apagado = false")
    Page<BigInteger> findAllIdsPaginado(Pageable pageable);

    @Query("SELECT i.idImovel FROM Imovel i WHERE i.apagado = false AND i.precoImovel <= :valorMax")
    Page<BigInteger> findDisponiveisIdsPorValorMax(@Param("valorMax") BigDecimal valorMax, Pageable pageable);

    // Etapa 2: Buscar imóveis completos com fotos e endereço
    @Query("""
            SELECT DISTINCT i FROM Imovel i
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.enderecoImovel
            WHERE i.idImovel IN :ids
            """)
    List<Imovel> findAllByIdInWithFotosAndEndereco(@Param("ids") List<BigInteger> ids);


    @EntityGraph(attributePaths = {"corretores", "imobiliarias"})
    Optional<Imovel> findByIdImovel(BigInteger idImovel);

    @EntityGraph(attributePaths = {"fotosImovel"})
    @Query("SELECT i FROM Imovel i WHERE i.idImovel = :id AND i.apagado = false")
    Optional<Imovel> findByIdActive(@Param("id") BigInteger id);

    @Query("SELECT i FROM Imovel i LEFT JOIN FETCH i.enderecoImovel LEFT JOIN FETCH i.fotosImovel WHERE i.idImovel = :id AND i.apagado = false")
    Optional<Imovel> findByIdWithEnderecoAndFotos(@Param("id") BigInteger id);

}
