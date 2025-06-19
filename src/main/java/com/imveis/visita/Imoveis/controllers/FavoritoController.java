package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImovelDTO;
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
    public ResponseEntity<Void> favoritar(@AuthenticationPrincipal UserDetailsImpl user,
                                          @PathVariable Long idImovel) {
        favoritoService.adicionarFavorito(user.getId(), idImovel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idImovel}")
    public ResponseEntity<Void> desfavoritar(@AuthenticationPrincipal UserDetailsImpl user,
                                             @PathVariable Long idImovel) {
        favoritoService.removerFavorito(user.getId(), idImovel);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ImovelDTO>> listarFavoritos(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(favoritoService.listarFavoritos(user.getId()));
    }
}

