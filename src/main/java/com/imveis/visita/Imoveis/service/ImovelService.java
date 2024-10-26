package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.EnderecoRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.UsuaRepository;
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
    private UsuaRepository usuaRepository;

    @Autowired
    public ImovelService(ImovelRepository imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    public List<Imovel> findAll() {
        return imovelRepository.findAll();
    }

    public Optional<Imovel> findById(BigInteger id) {
        return imovelRepository.findById(id);
    }

    public Imovel save(@org.jetbrains.annotations.NotNull Imovel imovel) {
        // Verifica se o Endereco está presente e precisa ser salvo
        if (imovel.getEnderecoImovel() != null) {
            Endereco endereco = imovel.getEnderecoImovel();
            if (endereco.getIdEndereco() == null) {
                Endereco enderecoSalvo = enderecoRepository.save(endereco);
                imovel.setEnderecoImovel(enderecoSalvo);
            }
        }
        // Verifica se o Usuario está presente e precisa ser salvo
        if (imovel.getUsuario() != null) {
            Usuario usuario = imovel.getUsuario();
            if (usuario.getIdUsuario() == null) {
                Usuario usuarioSalvo = usuaRepository.save(usuario);
                imovel.setUsuario(usuarioSalvo);
            }
        }
        if (imovel.getPrecoImovel() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo");
        }
        return imovelRepository.save(imovel);
    }

    public void deleteById(BigInteger id) {
        imovelRepository.deleteById(id);
    }
}
