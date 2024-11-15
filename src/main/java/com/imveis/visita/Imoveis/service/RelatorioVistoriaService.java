package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.repositories.FotoVistoriaRepository;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Optional;

/*import static org.graalvm.compiler.debug.TTY.out;*/


@Service
public class RelatorioVistoriaService {

    @Autowired
    private VistoriaRepository vistoriaRepository;

    @Autowired
    private FotoVistoriaRepository fotoVistoriaRepository;

    public ByteArrayInputStream gerarRelatorioVistorias(BigInteger imovelId) {
        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            Optional<Vistoria> vistorias = vistoriaRepository.findById(imovelId);

            document.add(new Paragraph("Relatório de Vistorias para Imóvel ID " + imovelId));

            for (Vistoria vistoria : vistorias.stream().toList()) {
                document.add(new Paragraph("Data da Vistoria: " + vistoria.getDataVistoria()));
                document.add(new Paragraph("Laudo: " + vistoria.getLaudoVistoria()));

                Optional<FotoVistoria> fotos = fotoVistoriaRepository.findById(vistoria.getIdVistoria());
                for (FotoVistoria foto : fotos.stream().toList()) {
                    document.add(new Paragraph("Foto URL: " + foto.getUrlFotoVistoria()));
                }
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* return new ByteArrayInputStream(out.toByteArray());*/
        return null;
    }
}
