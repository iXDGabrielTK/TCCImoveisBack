
package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.VisitanteRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

@Service
public class RelatorioUsuarioService {

    @Autowired
    private VisitanteRepository visitanteRepository;

    @Autowired
    private ImovelRepository imovelRepository;

    public ByteArrayInputStream gerarRelatorioUsuarios(BigInteger imovelId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(out)) {
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            long totalAcessos = visitanteRepository.count(); // Total de acessos
            BigInteger acessosImovel = imovelRepository.countAccessByImovelId(imovelId); // Total de acessos ao imóvel

            document.add(new Paragraph("Relatório de Usuários"));
            document.add(new Paragraph("Total de Acessos ao Site: " + totalAcessos));
            document.add(new Paragraph("Acessos ao Imóvel ID " + imovelId + ": " + acessosImovel));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
