package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioAgendamentoService {

    private final AgendaRepository agendaRepository;

    public RelatorioAgendamentoService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public ByteArrayInputStream gerarRelatorioAgendamentos(YearMonth mesAno) {
        try {
            // Carregar template Jasper (.jrxml)
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/reports/relatorio_agendamentos.jrxml"
            );

            // Extrair ano e mês do parâmetro
            int ano = mesAno.getYear();
            int mes = mesAno.getMonthValue();

            // Consultar os dados do banco para o período
            List<Object[]> agendamentos = agendaRepository.countAgendamentosByImovelAndMonth(ano, mes);

            // Transformar os dados em um DataSource para JasperReports
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(agendamentos);

            // Passar parâmetros para o relatório
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", "Relatório de Agendamentos");
            parametros.put("subtitulo", "Agendamentos no período: " + mesAno);

            // Preencher o relatório com dados
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // Exportar o relatório para PDF
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
