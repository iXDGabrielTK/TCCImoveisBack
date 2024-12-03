package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioUsuarioDTO;
import com.imveis.visita.Imoveis.entities.LogAcesso;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.LogAcessoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public long countLoginsByMonth(int ano, int mes) {
        return logAcessoRepository.countLoginsByMonth(ano, mes);
    }

    public List<Object[]> countLoginsByUsuario() {
        return logAcessoRepository.countLoginsByUsuario();
    }

    public List<RelatorioUsuarioDTO> buscarRelatorioUsuarios(int ano, int mes) {
        return logAcessoRepository.buscarRelatorioUsuarios(ano, mes);
    }
}
