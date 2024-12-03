package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.dtos.RelatorioAgendamentoDTO;
import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, BigInteger> {


    @Query("SELECT COUNT(a) FROM Agendamento a " +
            "WHERE EXTRACT(YEAR FROM a.dataAgendamento) = :ano " +
            "AND EXTRACT(MONTH FROM a.dataAgendamento) = :mes")
    long countAgendamentosByMonth(@Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT a.imovel.id AS imovelId, COUNT(a) AS totalAgendamentos " +
            "FROM Agendamento a " +
            "WHERE EXTRACT(YEAR FROM a.dataAgendamento) = :ano " +
            "AND EXTRACT(MONTH FROM a.dataAgendamento) = :mes " +
            "GROUP BY a.imovel.id")
    List<Object[]> countAgendamentosByImovelAndMonth(@Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT a FROM Agendamento a WHERE a.imovel.idImovel = :imovelId")
    List<Agendamento> findByImovelId(@Param("imovelId") BigInteger imovelId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Agendamento a " +
            "WHERE a.imovel.idImovel = :imovelId " +
            "AND a.dataAgendamento = :dataAgendamento " +
            "AND a.horarioMarcado = :horarioMarcado")
    boolean existsByImovelIdAndDataAgendamentoAndHorarioMarcado(
            @Param("imovelId") BigInteger imovelId,
            @Param("dataAgendamento") LocalDate dataAgendamento,
            @Param("horarioMarcado") boolean horarioMarcado
    );

    @Query("SELECT a FROM Agendamento a WHERE a.imovel.idImovel = :imovelId AND a.dataAgendamento = :data AND a.horarioMarcado = :horarioMarcado")
    Optional<Agendamento> findByImovelIdAndDataAgendamentoAndHorarioMarcado(
            @Param("imovelId") BigInteger imovelId,
            @Param("data") LocalDate data,
            @Param("horarioMarcado") boolean horarioMarcado
    );
    
    List<Agendamento> findByUsuarioId(BigInteger usuarioId);

    @Query("SELECT v FROM Visitante v WHERE v.id = :usuarioId")
    Optional<Visitante> findUsuarioById(@Param("usuarioId") BigInteger usuarioId);


    List<RelatorioAgendamentoDTO> buscarRelatorioAgendamentos(int ano, int mes);
}
