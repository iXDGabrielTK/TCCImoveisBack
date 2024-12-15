package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.LogAcesso;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.LogAcessoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogAcessoService {

    private final LogAcessoRepository logAcessoRepository;

    public LogAcessoService(LogAcessoRepository logAcessoRepository) {
        this.logAcessoRepository = logAcessoRepository;
    }

    public void registrarLog(Usuario usuario, String acao) {
        LogAcesso log = new LogAcesso();
        log.setUsuario(usuario);
        log.setDataHora(LocalDateTime.now());
        log.setAcao(acao);
        logAcessoRepository.save(log);
    }

}
