package com.imveis.visita.Imoveis.service;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioAgendamentoService {

    @Value("${relatorios.caminho}")
    private String caminhoRelatorios;

    public byte[] gerarRelatorioAgendamentos(BigInteger idImovel, String mesAno) {
        try {
            String caminhoRelatorio = caminhoRelatorios + "relatorio_agendamentos.jasper";
            File relatorio = new File(caminhoRelatorio);

            if (!relatorio.exists()) {
                throw new JRException("O arquivo do relatório não foi encontrado no caminho: " + caminhoRelatorio);
            }

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_IMOVEL", idImovel);
            parametros.put("MES_ANO", mesAno);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    relatorio.getAbsolutePath(),
                    parametros,
                    new JREmptyDataSource()
            );

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Erro ao gerar o relatório de agendamentos.", e);
        }
    }

}
