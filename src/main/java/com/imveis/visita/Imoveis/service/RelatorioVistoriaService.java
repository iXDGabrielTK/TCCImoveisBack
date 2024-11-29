package com.imveis.visita.Imoveis.service;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioVistoriaService {

    @Value("${relatorios.caminho}")
    private String caminhoRelatorios;

    public byte[] gerarRelatorioVistorias(BigInteger idImovel, String mesAno) {
        if (idImovel == null) {
            throw new IllegalArgumentException("O ID do imóvel não pode ser nulo.");
        }

        try {
            String caminhoRelatorio = caminhoRelatorios + "relatorio_vistorias.jasper";
            File relatorio = new File(caminhoRelatorio);

            if (!relatorio.exists()) {
                throw new JRException("O arquivo do relatório não foi encontrado no caminho: " + caminhoRelatorio);
            }

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_IMOVEL", idImovel);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    relatorio.getAbsolutePath(),
                    parametros,
                    new JREmptyDataSource() // Conecte ao seu datasource real aqui
            );

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Erro ao gerar o relatório de vistorias.", e);
        }
    }
}
