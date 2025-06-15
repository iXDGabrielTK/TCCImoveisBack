package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> findById(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario save(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deleteById(Long id) {
        funcionarioRepository.deleteById(id);
    }
}
