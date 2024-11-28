package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, BigInteger> {

    @Query("SELECT v FROM Visitante v WHERE v.login = :login AND v.senha = :senha")
    Optional<Visitante> findByLoginAndSenha(@Param("login") String login, @Param("senha") String senha);

    // Novo método: Busca visitantes por ano e mês de cadastro
    @Query("SELECT v.nome AS nome, v.dataCadastro AS dataCadastro " +
            "FROM Visitante v " +
            "WHERE EXTRACT(YEAR FROM v.dataCadastro) = :ano " +
            "AND EXTRACT(MONTH FROM v.dataCadastro) = :mes")
    List<Object[]> findUsuariosByMesAno(@Param("ano") int ano, @Param("mes") int mes);
}
