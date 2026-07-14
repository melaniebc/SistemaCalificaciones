package evaluacion;

import academico.Materia;
import usuarios.Estudiante;
import usuarios.Profesor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


 //Guarda trazabilidad cuando un profesor modifica una calificación.
 //Permite evidenciar control académico y auditoría básica.

public class HistorialNota {
    private Estudiante estudiante;
    private Materia materia;
    private Profesor profesor;
    private float notaAnterior;
    private float notaNueva;
    private String motivo;
    private LocalDateTime fechaCambio;

    public HistorialNota(Estudiante estudiante, Materia materia, Profesor profesor,
                         float notaAnterior, float notaNueva, String motivo) {
        this.estudiante = estudiante;
        this.materia = materia;
        this.profesor = profesor;
        this.notaAnterior = notaAnterior;
        this.notaNueva = notaNueva;
        this.motivo = motivo == null || motivo.isBlank() ? "Sin motivo registrado" : motivo;
        this.fechaCambio = LocalDateTime.now();
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Materia getMateria() {
        return materia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public float getNotaAnterior() {
        return notaAnterior;
    }

    public float getNotaNueva() {
        return notaNueva;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getFechaCambioTexto() {
        return fechaCambio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
