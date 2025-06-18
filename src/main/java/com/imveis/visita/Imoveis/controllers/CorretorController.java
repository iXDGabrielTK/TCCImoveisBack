package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.CorretorRequest;
import com.imveis.visita.Imoveis.dtos.CorretorResponse;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.CorretorService;
import com.imveis.visita.Imoveis.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorService corretorService;
    private final NotificacaoService notificacaoService;

    @PostMapping("/solicitar")
    @PreAuthorize("hasRole('VISITANTE')")
    public ResponseEntity<String> solicitarCorretor(@RequestBody CorretorRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (request.getCreci() == null || request.getCreci().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("CRECI não pode ser vazio.");
        }

        notificacaoService.notificarCorretor(userDetails.getId(), request.getCreci().trim());
        return ResponseEntity.ok("Solicitação enviada para análise dos funcionários.");
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> verificarSeEhCorretor(@PathVariable Long idUsuario) {
        Optional<CorretorResponse> corretor = corretorService.buscarPorUsuarioId(idUsuario);
        if (corretor.isPresent()) {
            return ResponseEntity.ok(corretor.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/{id}/arquivar")
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'CORRETOR')")
    public ResponseEntity<?> arquivar(@PathVariable Long id) {
        notificacaoService.arquivar(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<CorretorResponse>> getAllCorretores() {
        List<CorretorResponse> corretores = corretorService.buscarTodos();
        return ResponseEntity.ok(corretores);
    }
}