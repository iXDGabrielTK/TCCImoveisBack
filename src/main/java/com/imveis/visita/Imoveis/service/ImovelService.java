package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.EnderecoRepository;
import com.imveis.visita.Imoveis.repositories.FuncionarioRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @Autowired
    public ImovelService(ImovelRepository imovelRepository){
        this.imovelRepository = imovelRepository;
    }

    public List<Imovel> findAll() {
        return imovelRepository.findAll();
    }

    public Optional<Imovel> findById(BigInteger id) {
        return imovelRepository.findById(id);
    }

    public Imovel save(Imovel imovel) {
        // Verifica se o Endereco está presente e precisa ser salvo
        if (imovel.getEnderecoImovel() != null) {
            Endereco endereco = imovel.getEnderecoImovel();
            if (endereco.getIdEndereco() == null) {
                Endereco enderecoSalvo = enderecoRepository.save(endereco);
                imovel.setEnderecoImovel(enderecoSalvo);
            }
        }
        // Verifica se o Funcionario está presente e precisa ser salvo
        if (imovel.getFuncionario() != null) {
            Funcionario funcionario = imovel.getFuncionario();
            if (funcionario.getId() == null) {
                Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
                imovel.setFuncionario(funcionarioSalvo);
            }
        }
        // Valida o preço do imóvel
        if (imovel.getPrecoImovel() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo");
        }
        return imovelRepository.save(imovel);
    }

    public void deleteById(BigInteger id) {
        imovelRepository.deleteById(id);
    }
}
