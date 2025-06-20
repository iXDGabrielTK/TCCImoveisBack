package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.*;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.exceptions.BusinessException;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import com.imveis.visita.Imoveis.repositories.NotificacaoRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacaoServiceImpl implements NotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoServiceImpl.class);

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ImobiliariaRepository imobiliariaRepository;
    private final CorretorService corretorService;
    private final CorretorRepository corretorRepository;

    public NotificacaoServiceImpl(NotificacaoRepository repo, UsuarioRepository usuarioRepository, ImobiliariaRepository imobiliariaRepository, CorretorService corretorService, CorretorRepository corretorRepository) {
        this.notificacaoRepository = repo;
        this.usuarioRepository = usuarioRepository;
        this.imobiliariaRepository = imobiliariaRepository;
        this.corretorService = corretorService;
        this.corretorRepository = corretorRepository;
    }

    @Override
    public void notificarCorretor(Long idUsuario, String creci) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!(usuario instanceof Visitante visitante)) {
            throw new BusinessException("Somente visitantes podem solicitar corretor");
        }

        NotificacaoCorretor notif = new NotificacaoCorretor();
        notif.setNomeSolicitante(visitante.getNome());
        notif.setCreciSolicitado(creci);
        notif.setVisivelParaTodosFuncionarios(true);
        notif.setLida(false);
        notif.setDataCriacao(LocalDateTime.now());
        notif.setRemetente(visitante);
        notificacaoRepository.save(notif);
    }

    @Override
    public void notificarImobiliaria(Long id, String nomeCorretor, String nomeImobiliaria, String cnpj) {
        Corretor corretor = corretorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        NotificacaoImobiliaria n = new NotificacaoImobiliaria();
        n.setNomeCorretor(nomeCorretor);
        n.setNomeImobiliaria(nomeImobiliaria);
        n.setCnpj(cnpj);
        n.setVisivelParaTodosFuncionarios(true);
        n.setDataCriacao(LocalDateTime.now());
        n.setRemetente(corretor);
        n.setLida(false);
        notificacaoRepository.save(n);
    }

    @Override
    public void criarNotificacaoProposta(Proposta proposta) {
        Imovel imovel = proposta.getImovel();
        Corretor corretor = imovel.getImobiliaria().getCorretor();
        NotificacaoProposta notificacao = new NotificacaoProposta();
        notificacao.setProposta(proposta);
        notificacao.setDestinatario(corretor);
        notificacao.setLida(false);
        notificacao.setArquivada(false);
        notificacao.setDataCriacao(LocalDateTime.now());
        notificacao.setVisivelParaTodosFuncionarios(false);
        notificacaoRepository.save(notificacao);
    }

    @Override
    public void arquivar(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        notificacao.setArquivada(true);
        notificacaoRepository.save(notificacao);
    }

    @Override
    public List<NotificacaoDTO> listarVisiveisParaUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        List<Notificacao> todas = notificacaoRepository.findAllByOrderByDataCriacaoDesc();
        List<Notificacao> notificacoesVisiveis = new ArrayList<>();

        for (Notificacao n : todas) {
            boolean visivel = n.getDestinatario() != null && n.getDestinatario().getId().equals(usuarioId);

            if (n.isVisivelParaTodosFuncionarios() && usuario instanceof Funcionario) {
                visivel = true;
            }

            if (visivel) {
                notificacoesVisiveis.add(n);
            }
        }

        logger.info("[listarVisiveisParaUsuario] Usuário {} - IDs retornados: {}", usuarioId, notificacoesVisiveis.stream().map(Notificacao::getId).toList());

        return notificacoesVisiveis.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<NotificacaoDTO> listarSomentePrivadas(Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoRepository.findByDestinatarioIdAndLidaFalse(usuarioId);
        logger.info("[listarSomentePrivadas] Usuário {} - IDs retornados: {}", usuarioId, notificacoes.stream().map(Notificacao::getId).toList());
        return notificacoes.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<NotificacaoDTO> listarNaoLidas(Long usuarioId) {
        List<NotificacaoDTO> todasVisiveis = listarVisiveisParaUsuario(usuarioId);
        List<NotificacaoDTO> naoLidas = todasVisiveis.stream()
                .filter(n -> !n.isLida())
                .toList();

        logger.info("[listarNaoLidas] Usuário {} - IDs retornados: {}", usuarioId, naoLidas.stream().map(NotificacaoDTO::getId).toList());

        return naoLidas;
    }

    @Override
    public List<NotificacaoDTO> listarNaoLidasVisiveis(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        List<Notificacao> privadas = notificacaoRepository.findByDestinatarioIdAndLidaFalse(userId);
        List<Notificacao> publicas = new ArrayList<>();

        if (usuario instanceof Funcionario) {
            publicas = notificacaoRepository.findByVisivelParaTodosFuncionariosTrueAndLidaFalse();
        }

        logger.info("[listarNaoLidasVisiveis] Privadas para o usuário {}: {}", userId, privadas.stream().map(Notificacao::getId).toList());
        logger.info("[listarNaoLidasVisiveis] Públicas: {}", publicas.stream().map(Notificacao::getId).toList());

        List<Notificacao> todas = new ArrayList<>();
        todas.addAll(privadas);
        todas.addAll(publicas);

        List<NotificacaoDTO> dtos = todas.stream()
                .distinct()
                .map(this::toDTO)
                .toList();

        logger.info("[listarNaoLidasVisiveis] Total de notificações (após distinct): {}", dtos.size());

        return dtos;
    }

    private NotificacaoDTO toDTO(Notificacao n) {
        if (n instanceof NotificacaoCorretor nc) {
            NotificacaoCorretorDTO dto = new NotificacaoCorretorDTO();
            dto.setId(nc.getId());
            dto.setLida(nc.isLida());
            dto.setResumo(nc.getResumo());
            dto.setTipo("Corretor");
            dto.setDataCriacao(nc.getDataCriacao());
            dto.setNomeSolicitante(nc.getNomeSolicitante());
            dto.setCreciSolicitado(nc.getCreciSolicitado());
            dto.setNomeUsuario(nc.getRemetente() != null ? nc.getRemetente().getNome() : null);
            dto.setEmailUsuario(nc.getRemetente() != null ? nc.getRemetente().getEmail() : null);
            dto.setRespondida(nc.getRespondida());
            dto.setArquivada(nc.isArquivada());
            return dto;
        }
        if (n instanceof NotificacaoImobiliaria ni) {
            NotificacaoImobiliariaDTO dto = new NotificacaoImobiliariaDTO();
            dto.setId(ni.getId());
            dto.setLida(ni.isLida());
            dto.setResumo(ni.getResumo());
            dto.setTipo("Imobiliaria");
            dto.setDataCriacao(ni.getDataCriacao());
            dto.setNomeCorretor(ni.getNomeCorretor());
            dto.setNomeImobiliaria(ni.getNomeImobiliaria());
            dto.setCnpj(ni.getCnpj());
            dto.setArquivada(ni.isArquivada());
            return dto;
        }
        if (n instanceof NotificacaoProposta np) {
            NotificacaoProposta npFetched = notificacaoRepository.findNotificacaoPropostaWithAll(np.getId());
            Proposta p = npFetched.getProposta();
            NotificacaoPropostaDTO dto = new NotificacaoPropostaDTO();
            dto.setId(npFetched.getId());
            dto.setLida(npFetched.isLida());
            dto.setResumo(npFetched.getResumo());
            dto.setTipo("Proposta");
            dto.setDataCriacao(npFetched.getDataCriacao());
            dto.setIdProposta(p.getId());
            dto.setIdImovel(p.getImovel().getIdImovel());
            dto.setValorProposta(p.getValorFinanciamento());
            dto.setNomeProponente(p.getUsuario().getNome());
            dto.setEmailProponente(p.getUsuario().getEmail());
            dto.setTelefoneProponente(p.getUsuario().getTelefone());
            dto.setArquivada(npFetched.isArquivada());
            return dto;
        }
        if (n instanceof NotificacaoRespostaSolicitacao nr) {
            NotificacaoRespostaSolicitacaoDTO dto = new NotificacaoRespostaSolicitacaoDTO();
            dto.setId(nr.getId());
            dto.setLida(nr.isLida());
            dto.setResumo(nr.getResumo());
            dto.setTipo("RespostaSolicitacao");
            dto.setDataCriacao(nr.getDataCriacao());
            dto.setArquivada(nr.isArquivada());
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

        Usuario usuario = notificacao.getRemetente();
        Corretor corretor = corretorService.criarCorretorAPartirDeUsuario(usuario, notificacao.getCreciSolicitado());

        notificacao.setRespondida(true);
        notificacao.setAprovada(true);
        notificacao.setLida(true);
        notificacao.setDataResposta(LocalDateTime.now());
        notificacaoRepository.save(notificacao);

        criarRespostaSolicitacao(corretor, true, "CORRETOR");
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
        notificacao.setLida(true);
        notificacao.setDataResposta(LocalDateTime.now());
        notificacaoRepository.save(notificacao);

        criarRespostaSolicitacao(notificacao.getRemetente(), false, "CORRETOR");
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
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);

        Imobiliaria imobiliaria = imobiliariaRepository.findByCnpj(notificacao.getCnpj())
                .orElseThrow(() -> new BusinessException("Imobiliária não encontrada com o CNPJ informado."));

        if (imobiliaria.isAprovada()) {
            throw new BusinessException("Essa imobiliária já foi aprovada.");
        }

        imobiliaria.setAprovada(true);
        imobiliariaRepository.save(imobiliaria);
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
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);

        criarRespostaSolicitacao(notificacao.getRemetente(), false, "IMOBILIARIA");
    }


    @Override
    public void marcarComoLida(Long id) {
        Notificacao notif = notificacaoRepository.findById(id).orElseThrow();
        notif.setLida(true);
        notificacaoRepository.save(notif);
    }
}
