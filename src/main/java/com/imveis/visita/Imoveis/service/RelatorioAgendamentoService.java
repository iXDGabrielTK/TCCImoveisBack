package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioAgendamentoService {

    @Autowired
    private AgendaRepository agendaRepository;

    public ByteArrayInputStream gerarRelatorioAgendamentos(YearMonth mesAno) {
        try {
            // Caminho do template JRXML
            String templatePath = "src/main/resources/reports/RelatorioAgendamentos.jrxml";

            // Compila o template JRXML para um objeto JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);

            // Extrair ano e mês
            int ano = mesAno.getYear();
            int mes = mesAno.getMonthValue();

            // Obter os dados do relatório
            List<Object[]> rawResults = agendaRepository.countAgendamentosByImovelAndMonth(ano, mes);

            // Mapear parâmetros para o relatório
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ANO", ano);
            parametros.put("MES", mes);

            // Preencher o relatório com os dados
            JRDataSource dataSource = new JRBeanCollectionDataSource(rawResults);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // Exportar para PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
