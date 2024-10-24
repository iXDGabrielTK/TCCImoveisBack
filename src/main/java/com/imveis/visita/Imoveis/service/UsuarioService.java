package com.imveis.visita.Imoveis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.UsuaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuaRepository usuaRepository;

    @Autowired
    public UsuarioService(UsuaRepository UsuaRepository) {
        this.usuaRepository = UsuaRepository;
    }

    public List<Usuario> findAll() {
        return usuaRepository.findAll();
    }

    public Optional<Usuario> findById(BigInteger id) {
        return usuaRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuaRepository.save(usuario);
    }

    public void deleteById(BigInteger id) {
        usuaRepository.deleteById(id);
    }
}