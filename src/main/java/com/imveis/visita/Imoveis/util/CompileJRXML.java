package com.imveis.visita.Imoveis.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class CompileJRXML {

    public static void main(String[] args) {
        // Lista de arquivos .jrxml
        List<String> jrxmlFiles = Arrays.asList(
                "src/main/resources/reports/relatorio_agendamento.jrxml",
                "src/main/resources/reports/relatorio_usuarios.jrxml",
                "src/main/resources/reports/relatorio_vistorias.jrxml"
        );

        // Compilar todos os arquivos
        for (String jrxmlPath : jrxmlFiles) {
            try {
                compileJRXML(jrxmlPath);
            } catch (Exception e) {
                System.err.println("Erro ao compilar o arquivo: " + jrxmlPath);
                e.printStackTrace();
            }
        }
    }

    private static void compileJRXML(String jrxmlPath) throws JRException, FileNotFoundException {
        File jrxmlFile = new File(jrxmlPath);
        if (!jrxmlFile.exists()) {
            throw new FileNotFoundException("Arquivo JRXML não encontrado: " + jrxmlPath);
        }

        // Gerar o caminho do arquivo compilado .jasper
        String jasperPath = jrxmlPath.replace(".jrxml", ".jasper");

        // Compilar o arquivo .jrxml
        try {
            System.out.println("Compilando: " + jrxmlPath);
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            System.out.println("Arquivo compilado com sucesso: " + jasperPath);
        } catch (Exception e) {
            System.err.println("Erro ao compilar o arquivo: " + jrxmlPath);
            System.err.println("Detalhes do erro:");
            e.printStackTrace(); // Mostra o erro completo no console
            throw e; // Interrompe a execução se houver erro
        }
    }


}
