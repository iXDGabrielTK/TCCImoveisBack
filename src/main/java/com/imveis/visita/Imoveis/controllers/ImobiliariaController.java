
package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImobiliariaRequest;
import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import com.imveis.visita.Imoveis.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imobiliarias")
@RequiredArgsConstructor
public class ImobiliariaController {

    private final ImobiliariaRepository imobiliariaRepository;
    private final NotificacaoService notificacaoService;

    @PostMapping("/candidatura")
    @PreAuthorize("hasRole('CORRETOR')")
    public ResponseEntity<String> solicitarImobiliaria(
            @RequestBody ImobiliariaRequest request,
            @AuthenticationPrincipal Usuario corretor
    ) {
        if (request.getCnpj() == null || request.getCnpj().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("CNPJ não pode ser vazio.");
        }

        notificacaoService.notificarImobiliaria(
                corretor.getId(),
                corretor.getNome(),
                request.getNome(),
                request.getCnpj()
        );

        return ResponseEntity.ok("Solicitação enviada para análise dos funcionários.");
    }

    @GetMapping
    public List<Imobiliaria> listar(){
        return imobiliariaRepository.findAll();
    }
}