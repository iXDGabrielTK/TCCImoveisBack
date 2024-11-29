package com.imveis.visita.Imoveis.service;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioUsuarioService {

    @Value("${relatorios.caminho}")
    private String caminhoRelatorios;

    public byte[] gerarRelatorioUsuarios(BigInteger idImovel, String mesAno) {
        try {
            String caminhoRelatorio = caminhoRelatorios + "relatorio_usuarios.jasper";
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
            throw new RuntimeException("Erro ao gerar o relatório de usuários.", e);
        }
    }

}
