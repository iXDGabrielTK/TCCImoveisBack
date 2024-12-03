package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.configs.JwtUtil;
import com.imveis.visita.Imoveis.dtos.LoginRequest;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.repositories.FuncionarioRepository;
import com.imveis.visita.Imoveis.repositories.VisitanteRepository;
import com.imveis.visita.Imoveis.service.LogAcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios/login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final VisitanteRepository visitanteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final LogAcessoService logAcessoService;

    @Autowired
    public LoginController(JwtUtil jwtUtil, VisitanteRepository visitanteRepository, FuncionarioRepository funcionarioRepository, LogAcessoService logAcessoService) {
        this.jwtUtil = jwtUtil;
        this.visitanteRepository = visitanteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.logAcessoService = logAcessoService;
    }

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String login = loginRequest.getLogin();
        String senha = loginRequest.getSenha();

        // Verificar credenciais do funcionário
        Optional<Funcionario> funcionario = funcionarioRepository.findByLoginAndSenha(login, senha);
        if (funcionario.isPresent()) {
            String token = jwtUtil.gerarToken(login);
            Funcionario funcionarioEncontrado = funcionario.get();

            logAcessoService.registrarLog(funcionarioEncontrado, "LOGIN");

            return ResponseEntity.ok().body(Map.of(
                    "token", token,
                    "usuario_Id", funcionarioEncontrado.getId(),
                    "tipo", "funcionario"
            ));
        }

        // Verificar credenciais do visitante
        Optional<Visitante> visitante = visitanteRepository.findByLoginAndSenha(login, senha);
        if (visitante.isPresent()) {
            String token = jwtUtil.gerarToken(login);
            Visitante visitanteEncontrado = visitante.get();

            logAcessoService.registrarLog(visitanteEncontrado, "LOGIN");

            return ResponseEntity.ok().body(Map.of(
                    "token", token,
                    "usuario_Id", visitanteEncontrado.getId(),
                    "tipo", "visitante"
            ));
        }

        // Credenciais inválidas
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
