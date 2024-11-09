package com.imveis.visita.Imoveis.controllers;
import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.service.VistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vistorias")
public class VistoriaController {

    private final VistoriaService vistoriaService;

    @Autowired
    public VistoriaController(VistoriaService vistoriaService){
        this.vistoriaService = vistoriaService;
    }

    @GetMapping
    public List<Vistoria> getAllVistorias(){
        return vistoriaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Vistoria> getVistoriaById(@PathVariable BigInteger id) {
        return vistoriaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> createVistoria(@RequestBody Vistoria vistoria){
        try {
            Vistoria novaVistoria = vistoriaService.save(vistoria);
            return ResponseEntity.ok(novaVistoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vistoria: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVistoria(@PathVariable BigInteger id){
        vistoriaService.deleteById(id);
    }

}
