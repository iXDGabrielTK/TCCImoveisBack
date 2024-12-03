package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.dtos.RelatorioAgendamentoDTO;
import com.imveis.visita.Imoveis.dtos.RelatorioUsuarioDTO;
import com.imveis.visita.Imoveis.entities.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface LogAcessoRepository extends JpaRepository<LogAcesso, BigInteger> {

    @Query("SELECT COUNT(l) FROM LogAcesso l WHERE EXTRACT(YEAR FROM l.dataHora) = :ano AND EXTRACT(MONTH FROM l.dataHora) = :mes AND l.acao = 'LOGIN'")
    long countLoginsByMonth(@Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT l.usuario.id AS usuarioId, COUNT(l) AS totalAcessos " +
            "FROM LogAcesso l WHERE l.acao = 'LOGIN' GROUP BY l.usuario.id")
    List<Object[]> countLoginsByUsuario();

    @Query("SELECT new com.imveis.visita.Imoveis.dtos.RelatorioUsuarioDTO( " +
            "l.usuario.id, l.usuario.nome, COUNT(l)) " +
            "FROM LogAcesso l " +
            "WHERE l.acao = 'LOGIN' AND EXTRACT(YEAR FROM l.dataHora) = :ano AND EXTRACT(MONTH FROM l.dataHora) = :mes " +
            "GROUP BY l.usuario.id, l.usuario.nome")
    List<RelatorioUsuarioDTO> buscarRelatorioUsuarios(@Param("ano") int ano, @Param("mes") int mes);
}

