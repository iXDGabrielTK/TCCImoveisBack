package com.imveis.visita.Imoveis.repositories;

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

    // Contar quantos acessos houve no mês especificado
    @Query("SELECT COUNT(l) FROM LogAcesso l WHERE EXTRACT(YEAR FROM l.dataHora) = :ano AND EXTRACT(MONTH FROM l.dataHora) = :mes AND l.acao = 'LOGIN'")
    long countLoginsByMonth(@Param("ano") int ano, @Param("mes") int mes);

    // Contar quantos acessos individuais cada usuário fez
    @Query("SELECT l.usuario.id AS usuarioId, COUNT(l) AS totalAcessos FROM LogAcesso l WHERE l.acao = 'LOGIN' GROUP BY l.usuario.id")
    List<Object[]> countLoginsByUsuario();
}
