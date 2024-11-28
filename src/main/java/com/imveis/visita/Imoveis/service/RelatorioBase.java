/*package com.imveis.visita.Imoveis.service;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.ByteArrayOutputStream;

public abstract class RelatorioBase {

    protected PdfFont font;

    public RelatorioBase(){
        try{
            font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected Document criarDocumento(ByteArrayOutputStream out){
        try{
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);
            document.setMargins(20,20,20,20);
            return document;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    protected Paragraph criarTitulo(String titulo) {
        return new Paragraph(titulo)
                .setFont(font)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER);
    }

    // Método para criar um subtítulo padronizado
    protected Paragraph criarSubtitulo(String subtitulo) {
        return new Paragraph(subtitulo)
                .setFont(font)
                .setFontSize(14)
                .setItalic()
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT);
    }

    // Método para criar uma tabela padronizada
    protected Table criarTabela(float[] colunas) {
        Table table = new Table(colunas);
        table.setWidth(UnitValue.createPercentValue(100)); // 100% da largura
        return table;
    }

    // Método para adicionar cabeçalhos à tabela
    protected void adicionarCabecalhos(Table table, String... cabecalhos) {
        for (String cabecalho : cabecalhos) {
            table.addHeaderCell(new Paragraph(cabecalho).setBold());
        }
    }

}
*/