package com.imveis.visita.Imoveis.repositories;


import com.imveis.visita.Imoveis.entities.FotoImovel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FotoImovelRepository extends JpaRepository<FotoImovel, BigInteger>{

    @Modifying
    @Transactional
    @Query("DELETE FROM FotoImovel f WHERE f.imovel.idImovel = :imovelId")
    void deleteByImovelId(@Param("imovelId") BigInteger imovelId);

}
