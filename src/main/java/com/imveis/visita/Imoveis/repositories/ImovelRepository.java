package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, BigInteger> {

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.imovel.idImovel = :idImovel")
    BigInteger countAccessById(@Param("idImovel") BigInteger idImovel);

    @Query("SELECT i.idImovel AS idImovel, COUNT(v.id) AS totalAcessos " +
            "FROM Visitante v JOIN v.imovel i " +
            "WHERE YEAR(v.dataAcesso) = YEAR(:mesAno) AND MONTH(v.dataAcesso) = MONTH(:mesAno) " +
            "GROUP BY i.idImovel")
    Map<Long, Long> countAccessByImovelAndMonth(@Param("mesAno") LocalDateTime mesAno);


}
