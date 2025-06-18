package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.NotificacaoDTO;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService service) {
        this.notificacaoService = service;
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @PutMapping("/corretor/{id}/aprovar")
    public ResponseEntity<?> aprovarSolicitacaoCorretor(@PathVariable Long id) {
        notificacaoService.aprovarSolicitacaoCorretor(id);
        return ResponseEntity.ok("Solicitação de corretor aprovada com sucesso.");
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @PutMapping("/corretor/{id}/recusar")
    public ResponseEntity<?> recusarSolicitacaoCorretor(@PathVariable Long id) {
        notificacaoService.recusarSolicitacaoCorretor(id);
        return ResponseEntity.ok("Solicitação de corretor recusada.");
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @PutMapping("/imobiliaria/aprovar/{id}")
    public ResponseEntity<Void> aprovarImobiliaria(@PathVariable Long id) {
        notificacaoService.aprovarSolicitacaoImobiliaria(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @PutMapping("/imobiliaria/recusar/{id}")
    public ResponseEntity<Void> recusarImobiliaria(@PathVariable Long id) {
        notificacaoService.recusarSolicitacaoImobiliaria(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoDTO>> listarTodas(@AuthenticationPrincipal UserDetailsImpl user) {
        List<NotificacaoDTO> notificacoes = notificacaoService.listarVisiveisParaUsuario(user.getId());
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/privadas")
    public ResponseEntity<List<NotificacaoDTO>> listarPrivadas(@AuthenticationPrincipal UserDetailsImpl user) {
        List<NotificacaoDTO> notificacoes = notificacaoService.listarSomentePrivadas(user.getId());
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/nao-lidas")
    public ResponseEntity<List<NotificacaoDTO>> listarNaoLidas(@AuthenticationPrincipal UserDetailsImpl user) {
        List<NotificacaoDTO> notificacoes = notificacaoService.listarNaoLidas(user.getId());
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/todas-nao-lidas")
    public ResponseEntity<List<NotificacaoDTO>> listarTodasNaoLidas(@AuthenticationPrincipal UserDetailsImpl user) {
        List<NotificacaoDTO> notificacoes = notificacaoService.listarNaoLidasVisiveis(user.getId());
        return ResponseEntity.ok(notificacoes);
    }


    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.noContent().build();
    }

}

