package evaluacion;

import usuarios.Estudiante;
import usuarios.Profesor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


 //Representa el registro de calificaciones de un estudiante en una materia.

public class RegistroNota implements IEvaluador {
    private Estudiante estudiante;
    private Profesor profesor;
    private float nota1;
    private float nota2;
    private float nota3;
    private String observacion;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimaModificacion;

    public RegistroNota(Estudiante estudiante, Profesor profesor, float nota1, float nota2, float nota3, String observacion) {
        this.estudiante = estudiante;
        this.profesor = profesor;
        this.fechaRegistro = LocalDateTime.now();
        this.fechaUltimaModificacion = this.fechaRegistro;
        setNotas(nota1, nota2, nota3);
        setObservacion(observacion);
    }

    @Override
    public String calcularEstado(float promedio) {
        if (promedio >= 14) {
            return "Aprobado";
        }
        if (promedio >= 9) {
            return "Recuperación";
        }
        return "Reprobado";
    }

    public String calcularCodigoAprobacion() {
        float promedio = calcularPromedio();
        if (promedio >= 14) {
            return "A";
        }
        if (promedio >= 9) {
            return "R";
        }
        return "F";
    }

    public float calcularPromedio() {
        // Modelo simple: los tres componentes tienen igual ponderación.
        return (nota1 + nota2 + nota3) / 3f;
    }

    public void setNotas(float nota1, float nota2, float nota3) {
        validarNota(nota1);
        validarNota(nota2);
        validarNota(nota3);
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }

    private void validarNota(float nota) {
        if (nota < 0 || nota > 20) {
            throw new IllegalArgumentException("Las notas deben estar entre 0 y 20.");
        }
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public float getNota1() {
        return nota1;
    }

    public float getNota2() {
        return nota2;
    }

    public float getNota3() {
        return nota3;
    }

    public String getObservacion() {
        return observacion == null || observacion.isBlank() ? "Sin observación" : observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion == null || observacion.isBlank() ? "Sin observación" : observacion;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public String getFechaUltimaModificacionTexto() {
        return fechaUltimaModificacion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
