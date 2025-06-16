package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.NotificacaoCorretorDTO;
import com.imveis.visita.Imoveis.dtos.NotificacaoDTO;
import com.imveis.visita.Imoveis.dtos.NotificacaoImobiliariaDTO;
import com.imveis.visita.Imoveis.dtos.NotificacaoPropostaDTO;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.exceptions.BusinessException;
import com.imveis.visita.Imoveis.repositories.NotificacaoRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoServiceImpl implements NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public NotificacaoServiceImpl(NotificacaoRepository repo, AuthService authService, UsuarioRepository usuarioRepository) {
        this.notificacaoRepository = repo;
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void notificarCorretor(String nome, String creci) {
        NotificacaoCorretor notif = new NotificacaoCorretor();
        notif.setNomeSolicitante(nome);
        notif.setCreciSolicitado(creci);
        notif.setVisivelParaTodosFuncionarios(true);
        notif.setLida(false);
        notif.setDataCriacao(LocalDateTime.now());
        notificacaoRepository.save(notif);
    }

    @Override
    public void notificarImobiliaria(String nomeCorretor, String nomeImobiliaria, String cnpj) {
        NotificacaoImobiliaria n = new NotificacaoImobiliaria();
        n.setNomeCorretor(nomeCorretor);
        n.setNomeImobiliaria(nomeImobiliaria);
        n.setCnpj(cnpj);
        notificacaoRepository.save(n);
    }

    @Override
    public void criarNotificacaoProposta(Proposta proposta, Usuario destinatario) {
        NotificacaoProposta notificacao = new NotificacaoProposta();
        notificacao.setProposta(proposta);
        notificacao.setDestinatario(destinatario);
        notificacao.setDataCriacao(LocalDateTime.now());
        notificacao.setLida(false);
        notificacaoRepository.save(notificacao);
    }

    @Override
    public List<NotificacaoDTO> listarNaoLidas(Long usuarioId) {
        return notificacaoRepository.findByDestinatarioIdAndLidaFalse(usuarioId).stream()
                .map(this::toDTO)
                .toList();
    }

    private NotificacaoDTO toDTO(Notificacao n) {
        if (n instanceof NotificacaoCorretor nc) {
            NotificacaoCorretorDTO dto = new NotificacaoCorretorDTO();
            dto.setId(nc.getId());
            dto.setLida(nc.isLida());
            dto.setResumo(nc.getResumo());
            dto.setTipo("CORRETOR");
            dto.setDataCriacao(nc.getDataCriacao());
            dto.setNomeSolicitante(nc.getNomeSolicitante());
            dto.setCreciSolicitado(nc.getCreciSolicitado());
            return dto;
        }
        if (n instanceof NotificacaoImobiliaria ni) {
            NotificacaoImobiliariaDTO dto = new NotificacaoImobiliariaDTO();
            dto.setId(ni.getId());
            dto.setLida(ni.isLida());
            dto.setResumo(ni.getResumo());
            dto.setTipo("IMOBILIARIA");
            dto.setDataCriacao(ni.getDataCriacao());
            dto.setNomeCorretor(ni.getNomeCorretor());
            dto.setNomeImobiliaria(ni.getNomeImobiliaria());
            dto.setCnpj(ni.getCnpj());
            return dto;
        }
        if (n instanceof NotificacaoProposta np) {
            Proposta p = np.getProposta();
            NotificacaoPropostaDTO dto = new NotificacaoPropostaDTO();
            dto.setId(np.getId());
            dto.setLida(np.isLida());
            dto.setResumo(np.getResumo());
            dto.setTipo("PROPOSTA");
            dto.setDataCriacao(np.getDataCriacao());
            dto.setIdProposta(p.getId());
            dto.setIdImovel(p.getImovel().getIdImovel());
            dto.setValorProposta(p.getValorFinanciamento());
            dto.setNomeProponente(p.getUsuario().getNome());
            return dto;
        }

        throw new IllegalArgumentException("Tipo desconhecido: " + n.getClass());
    }

    private void criarRespostaSolicitacao(Usuario destinatario, boolean aprovada, String tipoSolicitacao) {
        NotificacaoRespostaSolicitacao resposta = new NotificacaoRespostaSolicitacao();
        resposta.setDestinatario(destinatario);
        resposta.setAprovada(aprovada);
        resposta.setTipoSolicitacao(tipoSolicitacao.toUpperCase());
        resposta.setDataCriacao(LocalDateTime.now());
        resposta.setLida(false);
        notificacaoRepository.save(resposta);
    }

    @Override
    public void aprovarSolicitacaoCorretor(Long id) {
        NotificacaoCorretor notificacao = (NotificacaoCorretor) notificacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        if (notificacao.isAprovadaOuRecusada()) {
            throw new BusinessException("Solicitação já foi processada.");
        }

        Visitante visitante = notificacao.getRemetente();
        visitante.setCreci(notificacao.getCreciSolicitado());

        authService.atribuirRoleParaUsuario(visitante, "ROLE_CORRETOR");
        usuarioRepository.save(visitante);

        notificacao.setRespondida(true);
        notificacao.setAprovada(true);
        notificacao.setDataResposta(LocalDateTime.now());
        notificacaoRepository.save(notificacao);

        criarRespostaSolicitacao(visitante, true, "CORRETOR");
    }


    @Override
    public void recusarSolicitacaoCorretor(Long id) {
        NotificacaoCorretor notificacao = (NotificacaoCorretor) notificacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        if (notificacao.isAprovadaOuRecusada()) {
            throw new BusinessException("Solicitação já foi processada.");
        }

        notificacao.setRespondida(true);
        notificacao.setAprovada(false);
        notificacao.setDataResposta(LocalDateTime.now());
        notificacaoRepository.save(notificacao);

        criarRespostaSolicitacao(notificacao.getRemetente(), false, "IMOBILIARIA");
    }

    @Override
    public void aprovarSolicitacaoImobiliaria(Long id) {
        NotificacaoImobiliaria notificacao = (NotificacaoImobiliaria) notificacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        if (notificacao.isAprovadaOuRecusada()) {
            throw new BusinessException("Solicitação já foi processada.");
        }

        notificacao.setRespondida(true);
        notificacao.setAprovada(true);
        notificacaoRepository.save(notificacao);

    }

    @Override
    public void recusarSolicitacaoImobiliaria(Long id) {
        NotificacaoImobiliaria notificacao = (NotificacaoImobiliaria) notificacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        if (notificacao.isAprovadaOuRecusada()) {
            throw new BusinessException("Solicitação já foi processada.");
        }

        notificacao.setRespondida(true);
        notificacao.setAprovada(false);
        notificacaoRepository.save(notificacao);
    }


    @Override
    public void marcarComoLida(Long id) {
        Notificacao notif = notificacaoRepository.findById(id).orElseThrow();
        notif.setLida(true);
        notificacaoRepository.save(notif);
    }
}

