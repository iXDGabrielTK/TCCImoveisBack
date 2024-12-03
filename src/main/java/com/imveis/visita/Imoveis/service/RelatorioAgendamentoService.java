package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.RelatorioAgendamentoDTO;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioAgendamentoService {

    @Autowired
    private AgendaRepository agendaRepository;

    public List<RelatorioAgendamentoDTO> buscarRelatorioAgendamentos(String mesAno) {
        // Divide o mesAno (exemplo: "2024-11") em ano e mês
        String[] parts = mesAno.split("-");
        int ano = Integer.parseInt(parts[0]);
        int mes = Integer.parseInt(parts[1]);

        // Busca os dados no repositório
        return agendaRepository.buscarRelatorioAgendamentos(ano, mes);
    }
}

