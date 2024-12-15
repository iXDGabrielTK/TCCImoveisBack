package com.imveis.visita.Imoveis.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class compileJRXML {

    public static void main(String[] args) {
        List<String> jrxmlFiles = Arrays.asList(
                "src/main/resources/reports/relatorio_agendamento.jrxml",
                "src/main/resources/reports/relatorio_usuarios.jrxml",
                "src/main/resources/reports/relatorio_vistorias.jrxml"
        );

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

        String jasperPath = jrxmlPath.replace(".jrxml", ".jasper");

        try {
            System.out.println("Compilando: " + jrxmlPath);
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            System.out.println("Arquivo compilado com sucesso: " + jasperPath);
        } catch (Exception e) {
            System.err.println("Erro ao compilar o arquivo: " + jrxmlPath);
            System.err.println("Detalhes do erro:");
            e.printStackTrace();
            throw e;
        }
    }

}
