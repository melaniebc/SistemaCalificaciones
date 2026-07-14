package servicios;

import academico.Materia;
import usuarios.Estudiante;
import usuarios.Profesor;


// Servicio de negocio para registrar y modificar calificaciones.


public class CalificacionService {

    public float convertirNota(String texto) {
        try {
            float nota = Float.parseFloat(texto.trim().replace(',', '.'));
            validarRangoNota(nota);
            return nota;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ingrese notas válidas. Ejemplo: 18.50");
        }
    }

    public void validarRangoNota(float nota) {
        if (nota < 0 || nota > 20) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 20.");
        }
    }

    public boolean registrarNotas(Profesor profesor, Materia materia, Estudiante estudiante,
                                  float nota1, float nota2, float nota3, String observacion) {
        validarDatosBase(profesor, materia, estudiante);
        return profesor.registrarNotas(materia, estudiante, nota1, nota2, nota3, observacion);
    }

    public boolean modificarNotas(Profesor profesor, Materia materia, Estudiante estudiante,
                                  float nota1, float nota2, float nota3, String observacion, String motivoCambio) {
        validarDatosBase(profesor, materia, estudiante);
        if (motivoCambio == null || motivoCambio.isBlank()) {
            throw new IllegalArgumentException("Ingrese el motivo del cambio para dejar trazabilidad.");
        }
        return profesor.modificarNotas(materia, estudiante, nota1, nota2, nota3, observacion, motivoCambio);
    }

    private void validarDatosBase(Profesor profesor, Materia materia, Estudiante estudiante) {
        if (profesor == null) {
            throw new IllegalArgumentException("No existe un profesor en sesión.");
        }
        if (materia == null) {
            throw new IllegalArgumentException("Seleccione una materia.");
        }
        if (estudiante == null) {
            throw new IllegalArgumentException("Seleccione un estudiante.");
        }
        if (!materia.estaInscrito(estudiante)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en la materia seleccionada.");
        }
        if (!profesor.puedeGestionar(materia)) {
            throw new IllegalArgumentException("El profesor no tiene permiso para gestionar esta materia.");
        }
    }
}
