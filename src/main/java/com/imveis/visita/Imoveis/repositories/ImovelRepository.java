package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ImovelRepository extends JpaRepository<Imovel, BigInteger> {

    Optional<Imovel> findByEnderecoImovel_RuaAndEnderecoImovel_NumeroAndEnderecoImovel_Bairro(String rua, String numero, String bairro);

    @Query("SELECT i FROM Imovel i WHERE i.apagado = false")
    @EntityGraph(attributePaths = {"fotosImovel", "enderecoImovel"})
    List<Imovel> findAllActiveComFotos();

    @EntityGraph(attributePaths = "vistorias")
    Optional<Imovel> findByIdImovel(BigInteger idImovel);

    @EntityGraph(attributePaths = {"fotosImovel"})
    @Query("SELECT i FROM Imovel i WHERE i.idImovel = :id AND i.apagado = false")
    Optional<Imovel> findByIdActive(@Param("id") BigInteger id);

    @Query("SELECT i FROM Imovel i LEFT JOIN FETCH i.enderecoImovel LEFT JOIN FETCH i.fotosImovel WHERE i.idImovel = :id AND i.apagado = false")
    Optional<Imovel> findByIdWithEnderecoAndFotos(@Param("id") BigInteger id);

    @EntityGraph(attributePaths = {"fotosImovel", "enderecoImovel"})
    @Query("SELECT i FROM Imovel i WHERE i.apagado = false")
    List<Imovel> findAllWithFotos();

}
