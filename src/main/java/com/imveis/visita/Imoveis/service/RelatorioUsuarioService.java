package com.imveis.visita.Imoveis.service;

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
public class RelatorioUsuarioService {

    private final LogAcessoService logAcessoService;

    public RelatorioUsuarioService(LogAcessoService logAcessoService) {
        this.logAcessoService = logAcessoService;
    }

    public ByteArrayInputStream gerarRelatorioUsuarios(YearMonth mesAno) {
        try {
            // Carregar o template Jasper
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/reports/relatorio_usuarios.jrxml");

            // Obter dados para o relatório
            long totalLogins = logAcessoService.contarLoginsPorMes(mesAno);
            List<Object[]> loginsPorUsuario = logAcessoService.contarLoginsPorUsuario();

            // Transformar em DataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(loginsPorUsuario);

            // Criar parâmetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("totalLogins", totalLogins);
            parametros.put("titulo", "Relatório de Usuários - " + mesAno);

            // Preencher o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // Exportar para PDF
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
