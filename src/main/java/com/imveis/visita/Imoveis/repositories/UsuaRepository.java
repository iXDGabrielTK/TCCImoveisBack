package com.imveis.visita.Imoveis.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.imveis.visita.Imoveis.entities.Usuario;
import java.math.BigInteger;

@Repository
public interface UsuaRepository extends JpaRepository<Usuario, BigInteger> {
    // Aqui você pode adicionar métodos de busca personalizados, se necessário.
}
