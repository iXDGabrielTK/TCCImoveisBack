// PropostaService.java
package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.PropostaRequest;
import com.imveis.visita.Imoveis.dtos.PropostaResponse;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.exceptions.BusinessException;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.PropostaRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PropostaService {

    private static final Logger logger = LoggerFactory.getLogger(PropostaService.class);


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

        if (usuario.getId() == null || request.getImovelId() == null) {
            throw new IllegalArgumentException("ID do usuário e do imóvel não podem ser nulos.");
        }

        Imovel imovel = imovelRepository.findByIdImovel(request.getImovelId())
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

        logger.info("Proposta criada com sucesso: {}", proposta);
        notificacaoService.criarNotificacaoProposta(proposta);

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

        List<Long> idsImobiliarias;
        if (imovel.getImobiliaria() != null) {
            idsImobiliarias = Collections.singletonList(imovel.getImobiliaria().getId());
        } else {
            idsImobiliarias = Collections.emptyList();
        }

        Map<String, List<Long>> resultado = new HashMap<>();
        resultado.put("corretores", idsCorretores);
        resultado.put("imobiliarias", idsImobiliarias);

        return resultado;
    }

}