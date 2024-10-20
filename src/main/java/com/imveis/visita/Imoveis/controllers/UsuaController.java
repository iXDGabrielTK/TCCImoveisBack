package com.imveis.visita.Imoveis.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuaController {

    @GetMapping("/api/rota")
    public String getDados() {
        return "Dados do Backend";
    }
}

