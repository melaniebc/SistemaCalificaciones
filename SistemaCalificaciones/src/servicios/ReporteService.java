package servicios;

import academico.Materia;
import evaluacion.RegistroNota;
import usuarios.Estudiante;
import util.SimplePdfGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ReporteService {
    private SimplePdfGenerator pdfGenerator = new SimplePdfGenerator();

    public void generarPDFNotasEstudiante(Path archivo, Estudiante estudiante) throws IOException {
        if (estudiante == null) {
            throw new IllegalArgumentException("No existe estudiante en sesión.");
        }

        List<String> lineas = new ArrayList<>();
        lineas.add("Escuela Politécnica Nacional - Módulo Académico");
        lineas.add("Reporte de calificaciones generado el " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        lineas.add("");
        lineas.add("Estudiante: " + estudiante.getNombre());
        lineas.add("ID: " + estudiante.getId());
        lineas.add("Correo: " + estudiante.getCorreo());
        lineas.add("Carrera: " + estudiante.getCarrera().getNombre());
        lineas.add("Nivel: " + estudiante.getNivel());
        lineas.add("");
        lineas.add("Código | Materia | Nota1 | Nota2 | Nota3 | Promedio | Estado | Observación");
        lineas.add("--------------------------------------------------------------------------------");

        int registros = 0;
        float suma = 0f;
        for (Materia materia : estudiante.getCarrera().getMaterias()) {
            if (!materia.estaInscrito(estudiante)) {
                continue;
            }
            RegistroNota registro = materia.obtenerRegistroPorEstudiante(estudiante);
            if (registro == null) {
                lineas.add(materia.getCodigo() + " | " + materia.getNombreMateria() + " | - | - | - | - | Pendiente | Sin registro");
            } else {
                registros++;
                suma += registro.calcularPromedio();
                lineas.add(materia.getCodigo() + " | " + materia.getNombreMateria()
                        + " | " + formato(registro.getNota1())
                        + " | " + formato(registro.getNota2())
                        + " | " + formato(registro.getNota3())
                        + " | " + formato(registro.calcularPromedio())
                        + " | " + registro.calcularEstado(registro.calcularPromedio())
                        + " | " + registro.getObservacion());
            }
        }

        lineas.add("");
        float promedioGeneral = registros == 0 ? 0f : suma / registros;
        lineas.add("Promedio general: " + formato(promedioGeneral));
        lineas.add("Estado general: " + (registros == 0 ? "Pendiente" : estadoGeneral(promedioGeneral)));
        lineas.add("");
        lineas.add("Documento generado automáticamente por el Sistema de Gestión de Calificaciones.");
        pdfGenerator.generar(archivo, "Reporte de Calificaciones", lineas);
    }

    private String estadoGeneral(float promedio) {
        if (promedio >= 14) {
            return "Aprobado";
        }
        if (promedio >= 9) {
            return "Recuperación";
        }
        return "Reprobado";
    }

    private String formato(float valor) {
        return String.format("%.2f", valor);
    }
}
