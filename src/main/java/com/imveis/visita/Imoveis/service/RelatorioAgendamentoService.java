package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioAgendamentoService extends RelatorioBase {

    @Autowired
    private AgendaRepository agendaRepository;

    public ByteArrayInputStream gerarRelatorioAgendamentos(YearMonth mesAno) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = criarDocumento(out);

        if (document == null) return null;

        try {
            // Adicionando título e subtítulo
            document.add(criarTitulo("Relatório de Agendamentos"));
            document.add(criarSubtitulo("Agendamentos durante o mês: " + mesAno));

            // Extrair ano e mês do YearMonth
            int ano = mesAno.getYear();
            int mes = mesAno.getMonthValue();

            // Total de agendamentos gerais
            long totalAgendamentos = agendaRepository.countAgendamentosByMonth(ano, mes);
            document.add(new Paragraph("Total de Agendamentos no Mês: " + totalAgendamentos));

            // Total de agendamentos por imóvel
            List<Object[]> rawResults = agendaRepository.countAgendamentosByImovelAndMonth(ano, mes);
            Map<BigInteger, Long> agendamentosPorImovel = rawResults.stream()
                    .collect(Collectors.toMap(
                            r -> (BigInteger) r[0], // ID do imóvel
                            r -> (Long) r[1] // Total de agendamentos
                    ));

            if (agendamentosPorImovel.isEmpty()) {
                document.add(new Paragraph("Nenhum agendamento encontrado para o mês."));
            } else {
                Table table = criarTabela(new float[]{1, 2});
                adicionarCabecalhos(table, "ID Imóvel", "Total Agendamentos");

                agendamentosPorImovel.forEach((idImovel, total) -> {
                    table.addCell(String.valueOf(idImovel));
                    table.addCell(String.valueOf(total));
                });

                document.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
