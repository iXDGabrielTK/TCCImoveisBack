package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @Autowired
    public EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public List<Endereco> getAllEnderecos(){
        return enderecoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Endereco> getEnderecoById(@PathVariable Long id) {
        return enderecoService.findById(id);
    }

    @PostMapping
    public Endereco createEndereco(@RequestBody Endereco endereco){
        return enderecoService.save(endereco);
    }

    @PutMapping
    public Endereco updateEndereco(@RequestBody Endereco endereco){return enderecoService.save(endereco);}

    @DeleteMapping("/{id}")
    public void deleteEndereco(@PathVariable Long id) {
        enderecoService.deleteById(id);
    }
}
