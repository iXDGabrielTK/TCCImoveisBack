// src/main/java/com/imveis/visita/Imoveis/service/FuncionarioService.java
package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> findById(BigInteger id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario save(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deleteById(BigInteger id) {
        funcionarioRepository.deleteById(id);
    }
}
