/*
package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.FotoVistoria;
import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.repositories.FotoVistoriaRepository;
import com.imveis.visita.Imoveis.repositories.VistoriaRepository;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.List;

@Service
public class RelatorioVistoriaService extends RelatorioBase {

    @Autowired
    private VistoriaRepository vistoriaRepository;

    @Autowired
    private FotoVistoriaRepository fotoVistoriaRepository;

    public ByteArrayInputStream gerarRelatorioVistorias(BigInteger idImovel) {
        // Inicializar o fluxo de saída para armazenar o PDF
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = criarDocumento(out);

        if (document == null) {
            return null; // Retorna null se o documento não puder ser criado
        }

        try {
            // Adicionar título ao relatório
            document.add(criarTitulo("Relatório de Vistorias"));

            // Adicionar subtítulo
            document.add(criarSubtitulo("Histórico de vistorias para o Imóvel ID: " + idImovel));

            // Buscar as vistorias associadas ao imóvel
            List<Vistoria> vistorias = vistoriaRepository.findByImovelId(idImovel);


            if (vistorias.isEmpty()) {
                document.add(new Paragraph("Nenhuma vistoria encontrada para o imóvel."));
            } else {
                // Criar tabela para exibir os dados das vistorias
                Table table = criarTabela(new float[]{1, 3, 3});
                adicionarCabecalhos(table, "ID", "Data", "Laudo");

                for (Vistoria vistoria : vistorias) {
                    table.addCell(String.valueOf(vistoria.getIdVistoria()));
                    table.addCell(vistoria.getDataVistoria().toString());
                    table.addCell(vistoria.getLaudoVistoria());
                }

                document.add(table);

                // Adicionar seção de fotos relacionadas às vistorias
                document.add(criarSubtitulo("Fotos das vistorias:"));

                for (Vistoria vistoria : vistorias) {
                    List<FotoVistoria> fotos = fotoVistoriaRepository.findByVistoria(vistoria);

                    if (fotos.isEmpty()) {
                        document.add(new Paragraph("Nenhuma foto registrada para a vistoria ID: " + vistoria.getIdVistoria()));
                    } else {
                        for (FotoVistoria foto : fotos) {
                            document.add(new Paragraph("Foto URL: " + foto.getUrlFotoVistoria()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close(); // Certifique-se de fechar o documento para liberar recursos
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
*/