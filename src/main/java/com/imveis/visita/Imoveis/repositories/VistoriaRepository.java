package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO;
import com.imveis.visita.Imoveis.entities.Vistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VistoriaRepository extends JpaRepository<Vistoria, Long> {

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO( " +
            "v.idVistoria, v.imovel.idImovel, v.imovel.descricaoImovel, v.dataVistoria, v.laudoVistoria) " +
            "FROM Vistoria v " +
            "WHERE v.imovel.idImovel = :idImovel " +
            "ORDER BY v.dataVistoria DESC")
    List<RelatorioVistoriaDTO> buscarRelatorioVistorias(@Param("idImovel") Long idImovel);


    @Query("SELECT v FROM Vistoria v WHERE v.apagado = false")
    List<Vistoria> findAllActive();

    @Query("SELECT v FROM Vistoria v WHERE v.idVistoria = :id AND v.apagado = false")
    Optional<Vistoria> findByIdActive(@Param("id") Long id);



}

