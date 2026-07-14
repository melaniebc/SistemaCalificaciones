package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


 //Generador PDF básico

public class SimplePdfGenerator {

    private static final Charset CODIFICACION =
            Charset.forName("windows-1252");

    private static final int MAX_CARACTERES_LINEA = 92;

    public void generar(
            Path archivo,
            String titulo,
            List<String> lineas
    ) throws IOException {

        if (archivo == null) {
            throw new IllegalArgumentException(
                    "La ruta del archivo PDF no puede ser nula."
            );
        }

        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }

        String tituloLimpio = limpiarTexto(titulo);

        List<String> lineasFinales = prepararLineas(lineas);

        StringBuilder contenido = new StringBuilder();

        contenido.append("BT\n");

        // Fuente y tamaño del título
        contenido.append("/F1 16 Tf\n");
        contenido.append("50 795 Td\n");
        contenido.append("(")
                .append(escapar(tituloLimpio))
                .append(") Tj\n");

        // Fuente y tamaño del contenido
        contenido.append("/F1 10 Tf\n");
        contenido.append("0 -25 Td\n");

        for (String linea : lineasFinales) {
            contenido.append("(")
                    .append(escapar(linea))
                    .append(") Tj\n");

            contenido.append("0 -15 Td\n");
        }

        contenido.append("ET\n");

        byte[] contenidoBytes =
                contenido.toString().getBytes(CODIFICACION);

        ByteArrayOutputStream pdf =
                new ByteArrayOutputStream();

        List<Integer> offsets =
                new ArrayList<>();


        escribir(pdf, "%PDF-1.4\n");

        offsets.add(pdf.size());
        escribir(
                pdf,
                "1 0 obj\n" +
                        "<< /Type /Catalog /Pages 2 0 R >>\n" +
                        "endobj\n"
        );

        offsets.add(pdf.size());
        escribir(
                pdf,
                "2 0 obj\n" +
                        "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n" +
                        "endobj\n"
        );

        offsets.add(pdf.size());
        escribir(
                pdf,
                "3 0 obj\n" +
                        "<<\n" +
                        "  /Type /Page\n" +
                        "  /Parent 2 0 R\n" +
                        "  /MediaBox [0 0 595 842]\n" +
                        "  /Resources <<\n" +
                        "      /Font << /F1 4 0 R >>\n" +
                        "  >>\n" +
                        "  /Contents 5 0 R\n" +
                        ">>\n" +
                        "endobj\n"
        );

        offsets.add(pdf.size());
        escribir(
                pdf,
                "4 0 obj\n" +
                        "<<\n" +
                        "  /Type /Font\n" +
                        "  /Subtype /Type1\n" +
                        "  /BaseFont /Helvetica\n" +
                        "  /Encoding /WinAnsiEncoding\n" +
                        ">>\n" +
                        "endobj\n"
        );

        offsets.add(pdf.size());
        escribir(
                pdf,
                "5 0 obj\n" +
                        "<< /Length " + contenidoBytes.length + " >>\n" +
                        "stream\n"
        );

        pdf.write(contenidoBytes);

        escribir(
                pdf,
                "endstream\n" +
                        "endobj\n"
        );

        int inicioXref = pdf.size();

        escribir(pdf, "xref\n");
        escribir(pdf, "0 6\n");
        escribir(pdf, "0000000000 65535 f \n");

        for (Integer offset : offsets) {
            escribir(
                    pdf,
                    String.format(
                            "%010d 00000 n \n",
                            offset
                    )
            );
        }

        escribir(
                pdf,
                "trailer\n" +
                        "<< /Size 6 /Root 1 0 R >>\n" +
                        "startxref\n" +
                        inicioXref + "\n" +
                        "%%EOF"
        );

        Files.write(
                archivo,
                pdf.toByteArray()
        );
    }

    private List<String> prepararLineas(
            List<String> lineas
    ) {
        List<String> resultado =
                new ArrayList<>();

        if (lineas == null) {
            return resultado;
        }

        for (String linea : lineas) {
            String lineaLimpia =
                    limpiarTexto(linea);

            if (lineaLimpia.length()
                    <= MAX_CARACTERES_LINEA) {

                resultado.add(lineaLimpia);

            } else {
                resultado.addAll(
                        dividir(
                                lineaLimpia,
                                MAX_CARACTERES_LINEA
                        )
                );
            }
        }

        return resultado;
    }

    private void escribir(
            ByteArrayOutputStream salida,
            String texto
    ) throws IOException {

        salida.write(
                texto.getBytes(CODIFICACION)
        );
    }

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("\r", "")
                .replace("\n", " ");
    }

    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("–", "-")
                .replace("—", "-")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("‘", "'")
                .replace("’", "'")
                .replace("…", "...")
                .replace("•", "-")
                .replace("\t", "    ");
    }

    private List<String> dividir(
            String texto,
            int maximo
    ) {
        List<String> partes =
                new ArrayList<>();

        if (texto == null || texto.isBlank()) {
            partes.add("");
            return partes;
        }

        String restante = texto.trim();

        while (restante.length() > maximo) {
            int corte =
                    restante.lastIndexOf(
                            ' ',
                            maximo
                    );

            if (corte <= 0) {
                corte = maximo;
            }

            partes.add(
                    restante.substring(
                            0,
                            corte
                    ).trim()
            );

            restante =
                    restante.substring(corte).trim();
        }

        if (!restante.isEmpty()) {
            partes.add(restante);
        }

        return partes;
    }
}