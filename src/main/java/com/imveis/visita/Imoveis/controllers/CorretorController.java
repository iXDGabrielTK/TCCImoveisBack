package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.CorretorRequest;
import com.imveis.visita.Imoveis.dtos.CorretorResponse;
import com.imveis.visita.Imoveis.service.CorretorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorService corretorService;

    @PostMapping("/candidatura")
    public ResponseEntity<?> candidatar(@RequestBody CorretorRequest request) {
        try {
            CorretorResponse response = corretorService.candidatarUsuarioParaCorretor(
                    request.getUsuarioId(), request.getCreci()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Dados inválidos: " + ex.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(500).body("Erro interno ao processar candidatura: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> verificarSeEhCorretor(@PathVariable BigInteger idUsuario) {
        Optional<CorretorResponse> corretor = corretorService.buscarPorUsuarioId(idUsuario);
        if (corretor.isPresent()) {
            return ResponseEntity.ok(corretor.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<CorretorResponse>> getAllCorretores() {
        List<CorretorResponse> corretores = corretorService.buscarTodos();
        return ResponseEntity.ok(corretores);
    }
}