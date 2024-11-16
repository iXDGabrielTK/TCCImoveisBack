package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, BigInteger> {

    // Conta o total de agendamentos no mês
    @Query("SELECT COUNT(a) FROM Agendamento a " +
            "WHERE EXTRACT(YEAR FROM a.dataAgendamento) = :ano " +
            "AND EXTRACT(MONTH FROM a.dataAgendamento) = :mes")
    long countAgendamentosByMonth(@Param("ano") int ano, @Param("mes") int mes);

    // Conta o total de agendamentos por imóvel no mês
    @Query("SELECT a.imovel.id AS imovelId, COUNT(a) AS totalAgendamentos " +
            "FROM Agendamento a " +
            "WHERE EXTRACT(YEAR FROM a.dataAgendamento) = :ano " +
            "AND EXTRACT(MONTH FROM a.dataAgendamento) = :mes " +
            "GROUP BY a.imovel.id")
    List<Object[]> countAgendamentosByImovelAndMonth(@Param("ano") int ano, @Param("mes") int mes);
}
