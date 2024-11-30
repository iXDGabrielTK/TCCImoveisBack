package com.imveis.visita.Imoveis.service;

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

    @Value("${relatorios.caminho}")
    private String caminhoRelatorios;

    @Autowired
    private DataSource dataSource;

    public byte[] gerarRelatorioAgendamentos(BigInteger idImovel, String mesAno) {
        try {
            // Validar e formatar o parâmetro mesAno
            mesAno = validarFormatoMesAno(mesAno);

            // Caminho do arquivo .jasper
            String caminhoRelatorio = caminhoRelatorios + "relatorio_agendamento.jasper";
            File relatorio = new File(caminhoRelatorio);

            if (!relatorio.exists()) {
                throw new JRException("Arquivo não encontrado no caminho: " + caminhoRelatorio);
            }

            // Buscar dados para preencher o relatório
            List<AgendamentoDTO> dados = buscarDadosParaRelatorio(idImovel, mesAno);

            // Criar o DataSource baseado nos dados obtidos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);

            // Configurar parâmetros para o relatório
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", "Relatório de Agendamentos");

            // Preencher o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    relatorio.getAbsolutePath(),
                    parametros,
                    dataSource
            );

            // Log para depurar páginas no JasperPrint
            System.out.println("Verificando os dados enviados ao relatório:");
            dados.forEach(d -> System.out.println("ImovelId: " + d.getImovelId() + ", TotalAgendamentos: " + d.getTotalAgendamentos()));

            if (dados.isEmpty()) {
                System.out.println("Nenhum dado encontrado. O relatório será gerado vazio.");
            }


            // Exportar o relatório para PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o relatório de agendamentos.", e);
        }
    }


    // Valida o formato do parâmetro mesAno e o ajusta para o formato correto (YYYY-MM)
    private String validarFormatoMesAno(String mesAno) {
        try {
            // Valida o formato exato "YYYY-MM"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            formatter.parse(mesAno); // Apenas tenta parsear, sem adicionar dia
            return mesAno;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("O parâmetro mesAno deve estar no formato 'YYYY-MM'. Valor recebido: " + mesAno);
        }
    }



    private List<AgendamentoDTO> buscarDadosParaRelatorio(BigInteger idImovel, String mesAno) {
        String sql = "SELECT imovel_id, COUNT(*) AS totalAgendamentos " +
                "FROM agendamento " +
                "WHERE imovel_id = 7 AND TO_CHAR(data_agendamento, 'YYYY-MM') = 2024-11" +
                "GROUP BY imovel_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, idImovel.longValue());
            ps.setString(2, mesAno);

            ResultSet rs = ps.executeQuery();

            List<AgendamentoDTO> resultados = new ArrayList<>();
            while (rs.next()) {
                Long imovelId = rs.getLong("imovel_id");
                Long totalAgendamentos = rs.getLong("totalAgendamentos");
                System.out.println("Imovel ID: " + imovelId + ", Total Agendamentos: " + totalAgendamentos); // Log dos dados
                resultados.add(new AgendamentoDTO(imovelId, totalAgendamentos));
            }

            if (resultados.isEmpty()) {
                System.out.println("Nenhum dado encontrado para os parâmetros fornecidos.");
            }

            return resultados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar dados para o relatório.", e);
        }
    }


    // Classe interna para representar os dados do relatório
    public static class AgendamentoDTO {
        private Long imovelId;
        private Long totalAgendamentos;

        public AgendamentoDTO(Long imovelId, Long totalAgendamentos) {
            this.imovelId = imovelId;
            this.totalAgendamentos = totalAgendamentos;
        }

        public Long getImovelId() {
            return imovelId;
        }

        public void setImovelId(Long imovelId) {
            this.imovelId = imovelId;
        }

        public Long getTotalAgendamentos() {
            return totalAgendamentos;
        }

        public void setTotalAgendamentos(Long totalAgendamentos) {
            this.totalAgendamentos = totalAgendamentos;
        }
    }
}
