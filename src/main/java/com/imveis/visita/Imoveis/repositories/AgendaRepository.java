package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, BigInteger> {

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

    List<Agendamento> findByUsuarioId(BigInteger usuarioId);

}
