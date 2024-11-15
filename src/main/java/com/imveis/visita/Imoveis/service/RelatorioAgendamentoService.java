package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Agendamento;
import com.imveis.visita.Imoveis.repositories.AgendaRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.List;

@Service
public class RelatorioAgendamentoService {

    @Autowired
    private AgendaRepository agendamentoRepository;

    public ByteArrayInputStream gerarRelatorioAgendamentos(BigInteger imovelId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(out)) {
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            List<Agendamento> agendamentos = agendamentoRepository.findByImovelId(imovelId);

            document.add(new Paragraph("Relatório de Agendamentos para Imóvel ID " + imovelId));
            document.add(new Paragraph("Total de Agendamentos: " + agendamentos.size()));

            for (Agendamento agendamento : agendamentos) {
                document.add(new Paragraph("Visitante: " + agendamento.getNomeVisitante()));
                document.add(new Paragraph("Data de Agendamento: " + agendamento.getDataAgendamento()));
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
