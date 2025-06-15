package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.controllers.AuthController;
import com.imveis.visita.Imoveis.dtos.PropostaRequest;
import com.imveis.visita.Imoveis.dtos.PropostaResponse;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.NotificacaoPropostaRepository;
import com.imveis.visita.Imoveis.repositories.PropostaRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PropostaService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    private final PropostaRepository propostaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ImovelRepository imovelRepository;

    private final NotificacaoPropostaRepository notificacaoPropostaRepository;

    public PropostaService(PropostaRepository propostaRepository, UsuarioRepository usuarioRepository, ImovelRepository imovelRepository, NotificacaoPropostaRepository notificacaoPropostaRepository) {
        this.propostaRepository = propostaRepository;
        this.usuarioRepository = usuarioRepository;
        this.imovelRepository = imovelRepository;
        this.notificacaoPropostaRepository = notificacaoPropostaRepository;
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public PropostaResponse criarProposta(PropostaRequest request, Usuario usuario) {
        BigDecimal valorFinanciado = request.getValorImovel().subtract(request.getEntrada());
        logger.info("Chegou aqui");

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

        logger.info("Chegou até aqui");

        propostaRepository.save(proposta);

        Map<String, List<Long>> responsaveis = buscarResponsaveisDoImovel(imovel.getIdImovel());
        List<Long> idsUsuariosNotificados = new ArrayList<>();
        idsUsuariosNotificados.addAll(responsaveis.get("corretores"));
        idsUsuariosNotificados.addAll(responsaveis.get("imobiliarias"));

        for (Long id : idsUsuariosNotificados) {
            usuarioRepository.findById(id).ifPresent(destinatario -> {
                NotificacaoProposta notificacao = new NotificacaoProposta();
                notificacao.setProposta(proposta);
                notificacao.setDestinatario(destinatario);
                notificacao.setDataCriacao(LocalDateTime.now());
                notificacao.setLida(false);
                notificacaoPropostaRepository.save(notificacao);
            });
        }

        return new PropostaResponse(proposta);
    }

    public Map<String, List<Long>> buscarResponsaveisDoImovel(Long idImovel) {
        Imovel imovel = imovelRepository.findByIdImovel(idImovel)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));

        List<Long> idsCorretores = imovel.getCorretores()
                .stream()
                .map(Corretor::getId)
                .map(id -> Long.valueOf(id.longValue()))
                .toList();

        List<Long> idsImobiliarias = imovel.getImobiliarias()
                .stream()
                .map(Imobiliaria::getId)
                .toList();

        Map<String, List<Long>> resultado = new HashMap<>();
        resultado.put("corretores", idsCorretores);
        resultado.put("imobiliarias", idsImobiliarias);

        return resultado;
    }


}
