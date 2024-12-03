package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO;
import com.imveis.visita.Imoveis.entities.Vistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface VistoriaRepository extends JpaRepository<Vistoria, BigInteger> {

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO( " +
            "v.imovel.idImovel, v.imovel.descricaoImovel, TO_CHAR(v.dataVistoria, 'YYYY-MM-DD'), v.laudoVistoria) " +
            "FROM Vistoria v " +
            "WHERE v.imovel.idImovel = :idImovel " +
            "ORDER BY v.dataVistoria DESC")
    List<RelatorioVistoriaDTO> buscarRelatorioVistorias(@Param("idImovel") BigInteger idImovel);

}


