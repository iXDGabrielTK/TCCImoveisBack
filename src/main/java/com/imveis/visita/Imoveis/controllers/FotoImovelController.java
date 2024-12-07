package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.service.FotoImovelService;
import com.imveis.visita.Imoveis.service.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fotosImovel")
public class FotoImovelController {

    private final FotoImovelService fotoImovelService;
    private final ImovelService imovelService;


    @Autowired
    public FotoImovelController(FotoImovelService fotoImovelService, ImovelService imovelService) {
        this.fotoImovelService = fotoImovelService;
        this.imovelService = imovelService;
    }

    @GetMapping
    public List<FotoImovel> getAllFotosImovel() {
        return fotoImovelService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<FotoImovel> getFotoImovelById(@PathVariable BigInteger id) {
        return fotoImovelService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createFotosImovel(@RequestBody String urls, @RequestParam BigInteger imovelId) {
        Imovel imovel = imovelService.findById(imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado"));

        List<FotoImovel> fotos = Arrays.stream(urls.split(","))
                .map(String::trim)
                .filter(url -> !url.isEmpty() && url.startsWith("http"))
                .map(url -> {
                    FotoImovel foto = new FotoImovel();
                    foto.setImovel(imovel);
                    foto.setUrlFotoImovel(url);
                    return fotoImovelService.save(foto);
                })
                .collect(Collectors.toList());

        if (fotos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma URL válida fornecida.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fotos);
    }


    @DeleteMapping("/{id}")
    public void deleteFotoImovel(@PathVariable BigInteger id) {
        fotoImovelService.deleteById(id);
    }
}
