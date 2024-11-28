/*package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.repositories.VisitanteRepository;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.YearMonth;

@Service
public class RelatorioUsuarioService extends RelatorioBase {

    @Autowired
    private VisitanteRepository visitanteRepository;

    public ByteArrayInputStream gerarRelatorioUsuarios(YearMonth mesAno) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = criarDocumento(out);

        if (document == null) return null;

        try {
            document.add(criarTitulo("Relatório de Usuários"));
            document.add(criarSubtitulo("Acessos durante o mês: " + mesAno));

            // Extrair ano e mês do YearMonth
            int ano = mesAno.getYear();
            int mes = mesAno.getMonthValue();

            // Consultar total de acessos ao site
            long totalAcessosSite = visitanteRepository.countAccessByMonth(ano, mes);
            document.add(new Paragraph("Total de Acessos ao Site: " + totalAcessosSite));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
*/