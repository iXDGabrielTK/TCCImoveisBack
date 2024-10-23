package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.service.ImovelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public Imovel criarImovel(@RequestBody Imovel imovel) {
        return imovelService.save(imovel);
    }
}
