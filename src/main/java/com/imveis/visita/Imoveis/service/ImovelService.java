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

    public List<Imovel> findAll() {
        return imovelRepository.findAll();
    }

    public Optional<Imovel> findById(BigInteger id) {
        return imovelRepository.findById(id);
    }

    public Imovel save(Imovel imovel) {
        System.out.println(imovel.getFuncionario());
        if (imovel.getEnderecoImovel() != null) {
            Endereco endereco = imovel.getEnderecoImovel();
            if (endereco.getIdEndereco() == null) {
                Endereco enderecoSalvo = enderecoRepository.save(endereco);
                imovel.setEnderecoImovel(enderecoSalvo);
            }
        }

        try {
            if (imovel.getFuncionario() != null) {
                Funcionario funcionario = imovel.getFuncionario();

                if (funcionario.getLogin() != null) {
                    Optional<Funcionario> funcionarioExistente = funcionarioRepository.findByLogin(funcionario.getLogin());

                    if (funcionarioExistente.isPresent()) {
                        System.out.println(imovel.getFuncionario());
                        imovel.setFuncionario(funcionarioExistente.get());
                    } else {
                        throw new IllegalArgumentException("Funcionário com o login fornecido não encontrado");
                    }
                } else {
                    throw new IllegalArgumentException("Login do funcionário é necessário para associá-lo ao imóvel");
                }
            }

            if (imovel.getPrecoImovel() < 0) {
                throw new IllegalArgumentException("O preço não pode ser negativo");
            }

            return imovelRepository.save(imovel);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Erro ao salvar imóvel: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao salvar imóvel", e);
        }
    }

    public void deleteById(BigInteger id) {
        imovelRepository.deleteById(id);
    }
}
