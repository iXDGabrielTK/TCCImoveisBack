package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Endereco;
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
        List<Imovel> imoveis = imovelRepository.findAllActive();
        imoveis.forEach(imovel -> imovel.getFotosImovel().size());
        return imoveis;
    }

    public Optional<Imovel> findById(BigInteger id) {
        return imovelRepository.findByIdActive(id);
    }

    public Imovel save(Imovel imovel) {
        if (imovel.getEnderecoImovel() != null) {
            Endereco endereco = imovel.getEnderecoImovel();
            if (endereco.getIdEndereco() == null) {
                Endereco enderecoSalvo = enderecoRepository.save(endereco);
                imovel.setEnderecoImovel(enderecoSalvo);
            }
        }

        if (imovel.getPrecoImovel() < 0) {
            throw new IllegalArgumentException("O preço do imóvel não pode ser negativo");
        }

        return imovelRepository.save(imovel);
    }

    public void cancelarImovel(BigInteger id) {
        Imovel imovel = imovelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum imóvel encontrado com o ID especificado."));
        imovel.setApagado(true);
        imovelRepository.save(imovel);
    }

    public void deleteById(BigInteger id) {
        imovelRepository.deleteById(id);
    }

    public Optional<Imovel> findByEndereco(String rua, String numero, String bairro) {
        return imovelRepository.findByEnderecoImovel_RuaAndEnderecoImovel_NumeroAndEnderecoImovel_Bairro(rua, numero, bairro);
    }
}
