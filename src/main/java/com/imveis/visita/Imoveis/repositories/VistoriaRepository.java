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


    @Query("""
            SELECT DISTINCT v FROM Vistoria v
            JOIN FETCH v.imovel i
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.enderecoImovel
            LEFT JOIN FETCH i.imobiliaria
            LEFT JOIN FETCH i.corretores
            LEFT JOIN FETCH v.ambientes a
            LEFT JOIN FETCH a.fotos
            WHERE v.apagado = false
            """)
    List<Vistoria> findAllActiveWithAllDetails();

    @Query("""
            SELECT DISTINCT v FROM Vistoria v
            JOIN FETCH v.imovel i
            LEFT JOIN FETCH i.fotosImovel
            LEFT JOIN FETCH i.enderecoImovel
            LEFT JOIN FETCH i.imobiliaria
            LEFT JOIN FETCH i.corretores
            LEFT JOIN FETCH v.ambientes a
            LEFT JOIN FETCH a.fotos
            WHERE v.idVistoria = :id AND v.apagado = false
            """)
    Optional<Vistoria> findByIdActiveWithAllDetails(@Param("id") Long id);

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioVistoriaDTO(" +
            "v.idVistoria, v.imovel.idImovel, COALESCE(v.imovel.descricaoImovel, 'N/A'), v.dataVistoria, v.laudoVistoria) " +
            "FROM Vistoria v " +
            "LEFT JOIN v.imovel imovel " +
            "WHERE v.apagado = false " +
            "ORDER BY v.dataVistoria DESC")
    List<RelatorioVistoriaDTO> findAllActiveVistoriasForSelection();
}