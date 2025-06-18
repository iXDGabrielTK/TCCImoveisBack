
package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImobiliariaRequest;
import com.imveis.visita.Imoveis.entities.Corretor;
import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.exceptions.BusinessException;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imobiliaria")
@RequiredArgsConstructor
public class ImobiliariaController {

    private final ImobiliariaRepository imobiliariaRepository;
    private final NotificacaoService notificacaoService;
    private final CorretorRepository corretorRepository;

    @PostMapping("/candidatura")
    @PreAuthorize("hasRole('CORRETOR')")
    public ResponseEntity<String> solicitarImobiliaria(
            @RequestBody ImobiliariaRequest request,
            @AuthenticationPrincipal UserDetailsImpl usuario
    ) {
        if (request.getCnpj() == null || request.getCnpj().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("CNPJ não pode ser vazio.");
        }

        Corretor corretor = corretorRepository.findById(usuario.getId())
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        Imobiliaria novaImobiliaria = new Imobiliaria();
        novaImobiliaria.setNome(request.getNome());
        novaImobiliaria.setRazaoSocial(request.getRazaoSocial());
        novaImobiliaria.setCnpj(request.getCnpj());
        novaImobiliaria.setEmail(request.getEmail());
        novaImobiliaria.setCep(request.getCep());
        novaImobiliaria.setCorretor(corretor);
        novaImobiliaria.setAprovada(false);

        imobiliariaRepository.save(novaImobiliaria);

        notificacaoService.notificarImobiliaria(
                corretor.getId(),
                corretor.getNome(),
                request.getNome(),
                request.getCnpj()
        );

        return ResponseEntity.ok("Solicitação enviada para análise dos funcionários.");
    }

    @GetMapping("/imobiliarias-aprovadas")
    @PreAuthorize("hasRole('CORRETOR')")
    public List<Imobiliaria> buscarImobiliariasDoCorretorAprovadas(@AuthenticationPrincipal Usuario usuario) {
        Corretor corretor = corretorRepository.findById(usuario.getId())
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        return imobiliariaRepository.findByCorretorAndAprovadaTrue(corretor);
    }


    @GetMapping
    public List<Imobiliaria> listar(){
        return imobiliariaRepository.findAll();
    }
}