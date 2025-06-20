package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.PropostaRequest;
import com.imveis.visita.Imoveis.dtos.PropostaResponse;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.exceptions.BusinessException;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.PropostaRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PropostaService {



    private final PropostaRepository propostaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ImovelRepository imovelRepository;

    private final NotificacaoService notificacaoService;

    public PropostaService(PropostaRepository propostaRepository, UsuarioRepository usuarioRepository, ImovelRepository imovelRepository, NotificacaoService notificacaoService) {
        this.propostaRepository = propostaRepository;
        this.usuarioRepository = usuarioRepository;
        this.imovelRepository = imovelRepository;
        this.notificacaoService = notificacaoService;
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public PropostaResponse criarProposta(PropostaRequest request, Usuario usuario) {
        BigDecimal valorFinanciado = request.getValorImovel().subtract(request.getEntrada());

        Imovel imovel = imovelRepository.findById(request.getIdImovel())
                .orElseThrow(() -> new BusinessException("Imóvel não encontrado"));

        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setImovel(imovel);
        proposta.setRendaMensal(request.getRendaMensal());
        proposta.setEntrada(request.getEntrada());
        proposta.setValorImovel(request.getValorImovel());
        proposta.setValorFinanciamento(valorFinanciado);
        proposta.setDataProposta(LocalDate.now());
        proposta.setPrazo(request.getPrazo());

        propostaRepository.save(proposta);

        Map<String, List<Long>> responsaveis = buscarResponsaveisDoImovel(imovel.getIdImovel());

        responsaveis.getOrDefault("corretores", List.of()).forEach(id ->
                usuarioRepository.findById(id).ifPresent(destinatario ->
                        notificacaoService.criarNotificacaoProposta(proposta, destinatario)
                )
        );

        responsaveis.getOrDefault("imobiliarias", List.of()).forEach(id ->
                usuarioRepository.findById(id).ifPresent(destinatario ->
                        notificacaoService.criarNotificacaoProposta(proposta, destinatario)
                )
        );

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