package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.PropostaRequest;
import com.imveis.visita.Imoveis.dtos.PropostaResponse;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.NotificacaoPropostaRepository;
import com.imveis.visita.Imoveis.repositories.PropostaRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PropostaService {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private NotificacaoPropostaRepository notificacaoPropostaRepository;

    public Optional<Usuario> findById(BigInteger id) {
        return usuarioRepository.findById(id);
    }

    public PropostaResponse criarProposta(PropostaRequest request, Usuario usuario) {
        BigDecimal valorFinanciado = request.getValorImovel().subtract(request.getEntrada());

        Imovel imovel = imovelRepository.findById(request.getIdImovel())
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setImovel(imovel);
        proposta.setRendaMensal(request.getRendaMensal());
        proposta.setEntrada(request.getEntrada());
        proposta.setValorImovel(request.getValorImovel());
        proposta.setValorFinanciamento(valorFinanciado);
        proposta.setDataProposta(LocalDate.now());

        propostaRepository.save(proposta);

        Map<String, List<BigInteger>> responsaveis = buscarResponsaveisDoImovel(imovel.getIdImovel());
        List<BigInteger> idsUsuariosNotificados = new ArrayList<>();
        idsUsuariosNotificados.addAll(responsaveis.get("corretores"));
        idsUsuariosNotificados.addAll(responsaveis.get("imobiliarias"));

        for (BigInteger id : idsUsuariosNotificados){
            usuarioRepository.findById(id).ifPresent(destinatario -> {
                NotificacaoProposta notificacao = new NotificacaoProposta();
                notificacao.setProposta(proposta);
                notificacao.setDestinatario(destinatario);
                notificacao.setDataEnvio(LocalDateTime.now());
                notificacao.setLida(false);
                notificacaoPropostaRepository.save(notificacao);
            });
        }

        return new PropostaResponse(proposta);
    }
    public Map<String, List<BigInteger>> buscarResponsaveisDoImovel(BigInteger idImovel) {
        Imovel imovel = imovelRepository.findByIdImovel(idImovel)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));

        List<BigInteger> idsCorretores = imovel.getCorretores()
                .stream()
                .map(Corretor::getId)
                .toList();

        List<BigInteger> idsImobiliarias = imovel.getImobiliarias()
                .stream()
                .map(Imobiliaria::getId)
                .map(BigInteger::valueOf) // conversão Long -> BigInteger
                .toList();

        Map<String, List<BigInteger>> resultado = new HashMap<>();
        resultado.put("corretores", idsCorretores);
        resultado.put("imobiliarias", idsImobiliarias);

        return resultado;
    }


}
