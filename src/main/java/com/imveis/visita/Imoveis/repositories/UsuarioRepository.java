package com.imveis.visita.Imoveis.repositories;

import com.imveis.visita.Imoveis.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, BigInteger> {

    @Query("SELECT u FROM Usuario u WHERE TYPE(u) = :tipo")
    List<Usuario> findByTipo(@Param("tipo") Class<? extends Usuario> tipo);

}
