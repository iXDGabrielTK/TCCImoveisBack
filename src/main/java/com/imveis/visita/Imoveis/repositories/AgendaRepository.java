package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, BigInteger> {

    boolean existsByImovelAndDataAgendamentoAndHorarioMarcado(Imovel imovel, Date dataAgendamento,Boolean horarioMarcado);

    @Query("SELECT a FROM Agendamento a WHERE a.imovel.idImovel = :imovelId")
    List<Agendamento> findByImovelId(@Param("imovelId") BigInteger imovelId);
}
