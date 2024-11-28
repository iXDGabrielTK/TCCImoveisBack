package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.LogAcesso;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.LogAcessoRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class LogAcessoService {

    private final LogAcessoRepository logAcessoRepository;

    public LogAcessoService(LogAcessoRepository logAcessoRepository) {
        this.logAcessoRepository = logAcessoRepository;
    }

    // Registrar um novo log
    public void registrarLog(Usuario usuario, String acao) {
        LogAcesso log = new LogAcesso();
        log.setUsuario(usuario);
        log.setDataHora(LocalDateTime.now());
        log.setAcao(acao);
        logAcessoRepository.save(log);
    }

    // Contar logins por mês
    public long contarLoginsPorMes(YearMonth mesAno) {
        return logAcessoRepository.countLoginsByMonth(mesAno.getYear(), mesAno.getMonthValue());
    }

    // Consultar logins por usuário
    public List<Object[]> contarLoginsPorUsuario() {
        return logAcessoRepository.countLoginsByUsuario();
    }
}
