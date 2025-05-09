package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.PropostaRequest;
import com.imveis.visita.Imoveis.dtos.PropostaResponse;
import com.imveis.visita.Imoveis.entities.Proposta;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.PropostaRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PropostaService {

    @Autowired
    private PropostaRepository propostaRepository;


    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findById(BigInteger id) {
        return usuarioRepository.findById(id);
    }

    public PropostaResponse criarProposta(PropostaRequest request, Usuario usuario) {
        BigDecimal valorFinanciado = request.getValorImovel().subtract(request.getEntrada());

        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setRendaMensal(request.getRendaMensal());
        proposta.setEntrada(request.getEntrada());
        proposta.setValorImovel(request.getValorImovel());
        proposta.setValorFinanciamento(valorFinanciado);
        proposta.setDataProposta(LocalDate.now());

        propostaRepository.save(proposta);

        return new PropostaResponse(proposta);
    }

}
