package com.imveis.visita.Imoveis.repositories;


import com.imveis.visita.Imoveis.dtos.RelatorioPropostaDTO;
import com.imveis.visita.Imoveis.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioPropostaDTO(" +
            "p.id, p.usuario.nome, p.imovel.idImovel, p.imovel.descricaoImovel, p.valorImovel, p.entrada, p.rendaMensal, p.valorFinanciamento, p.prazo, p.dataProposta, 'Em Análise') " + // Inclui o novo campo p.prazo
            "FROM Proposta p " +
            "WHERE EXTRACT(YEAR FROM p.dataProposta) = :ano " +
            "AND EXTRACT(MONTH FROM p.dataProposta) = :mes " +
            "ORDER BY p.dataProposta DESC")
    List<RelatorioPropostaDTO> buscarRelatorioPropostasPorMesAno(@Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioPropostaDTO(" +
            "p.id, p.usuario.nome, p.imovel.idImovel, p.imovel.descricaoImovel, p.valorImovel, p.entrada, p.rendaMensal, p.valorFinanciamento, p.prazo, p.dataProposta, 'Em Análise') " + // Inclui o novo campo p.prazo
            "FROM Proposta p " +
            "WHERE EXTRACT(YEAR FROM p.dataProposta) = :ano " +
            "ORDER BY p.dataProposta DESC")
    List<RelatorioPropostaDTO> buscarRelatorioPropostasPorAno(@Param("ano") int ano);
}