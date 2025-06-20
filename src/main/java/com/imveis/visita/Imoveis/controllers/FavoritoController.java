package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.FavoritoDTO;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @PostMapping("/{idImovel}")
    public ResponseEntity<Void> favoritar(
            @PathVariable Long idImovel,
            @AuthenticationPrincipal UserDetailsImpl usuario
    ) {
        Long usuarioId = usuario.getId();
        favoritoService.favoritar(usuarioId, idImovel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idImovel}")
    public ResponseEntity<Void> desfavoritar(
            @PathVariable Long idImovel,
            @AuthenticationPrincipal UserDetailsImpl usuario
    ) {
        Long usuarioId = usuario.getId();
        favoritoService.desfavoritar(usuarioId, idImovel);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritoDTO>> listar(
            @AuthenticationPrincipal UserDetailsImpl usuario
    ) {
        Long usuarioId = usuario.getId();
        return ResponseEntity.ok(favoritoService.listarFavoritos(usuarioId));
    }
}
