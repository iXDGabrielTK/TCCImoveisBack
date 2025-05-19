package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.PropostaRequest;
import com.imveis.visita.Imoveis.dtos.PropostaResponse;
import com.imveis.visita.Imoveis.entities.NotificacaoProposta;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.NotificacaoPropostaRepository;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.PropostaService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private PropostaService propostaService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NotificacaoPropostaRepository notificacaoPropostaRepository;

    @PreAuthorize("hasAnyRole('ROLE_VISITANTE', 'ROLE_FUNCIONARIO')")
    @PostMapping
    public ResponseEntity<PropostaResponse> criarProposta(
            @RequestBody PropostaRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(new PropostaResponse("Usuário não autenticado. Faça login para enviar uma proposta."));
        }

        BigInteger usuarioId = userDetails.getId();
        try {

            logger.info("🔐 Tentando criar proposta para o usuário: {}", usuarioId);

            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            PropostaResponse response = propostaService.criarProposta(request, usuario);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao criar proposta", e);
            return ResponseEntity.internalServerError()
                    .body(new PropostaResponse("Erro Interno ao processar a proposta. Tente novamente mais tarde."));
        }
    }

    @GetMapping("/responsaveis/{idImovel}")
    public ResponseEntity<Map<String, List<BigInteger>>> listarResponsaveis(@PathVariable BigInteger idImovel) {
        Map<String, List<BigInteger>> resposta = propostaService.buscarResponsaveisDoImovel(idImovel);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/notificacoes")
    public List<NotificacaoProposta> getNotificacoes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Usuario usuario = usuarioService.buscarPorId(userDetails.getId());
        return notificacaoPropostaRepository.findByDestinatario(usuario);
    }
}
