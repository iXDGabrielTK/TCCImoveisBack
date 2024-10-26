package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.service.ImovelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @GetMapping
    public List<Imovel> getAllImoveis() {
        return imovelService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Imovel> getImoveisById(@PathVariable BigInteger id) {
        return imovelService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Imovel> criarImovel(@RequestBody Imovel imovel) {
        try {
            Imovel novoImovel = imovelService.save(imovel);
            return new ResponseEntity<>(novoImovel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteImovel(@PathVariable BigInteger id) {
        imovelService.deleteById(id);
    }
}
