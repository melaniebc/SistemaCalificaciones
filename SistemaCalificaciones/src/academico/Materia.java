package academico;

import evaluacion.HistorialNota;
import evaluacion.RegistroNota;
import usuarios.Estudiante;
import usuarios.Profesor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Materia {
    private String codigo;
    private String nombreMateria;
    private String paralelo;
    private String periodo;
    private Carrera carrera;
    private Profesor profesor;
    private List<Estudiante> estudiantes;
    private List<RegistroNota> registrosNotas;
    private List<HistorialNota> historialNotas;

    public Materia(String codigo, String nombreMateria, String paralelo, String periodo, Carrera carrera, Profesor profesor) {
        this.codigo = codigo;
        this.nombreMateria = nombreMateria;
        this.paralelo = paralelo;
        this.periodo = periodo;
        this.carrera = carrera;
        this.profesor = profesor;
        this.estudiantes = new ArrayList<>();
        this.registrosNotas = new ArrayList<>();
        this.historialNotas = new ArrayList<>();

        if (carrera != null) {
            carrera.agregarMateria(this);
        }
        if (profesor != null) {
            profesor.asignarMateria(this);
        }
    }

    public boolean inscribirEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiantes.contains(estudiante)) {
            return false;
        }
        return estudiantes.add(estudiante);
    }

    public boolean estaInscrito(Estudiante estudiante) {
        return estudiantes.contains(estudiante);
    }

    public boolean registrarNotas(Estudiante estudiante, Profesor profesor, float nota1, float nota2, float nota3, String observacion) {
        validarEstudianteInscrito(estudiante);
        if (obtenerRegistroPorEstudiante(estudiante) != null) {
            return false;
        }
        registrosNotas.add(new RegistroNota(estudiante, profesor, nota1, nota2, nota3, observacion));
        return true;
    }

    public boolean modificarNotas(Estudiante estudiante, Profesor profesor, float nota1, float nota2, float nota3,
                                  String observacion, String motivoCambio) {
        validarEstudianteInscrito(estudiante);
        RegistroNota registro = obtenerRegistroPorEstudiante(estudiante);
        if (registro == null) {
            return false;
        }

        float promedioAnterior = registro.calcularPromedio();
        registro.setNotas(nota1, nota2, nota3);
        registro.setObservacion(observacion);
        float promedioNuevo = registro.calcularPromedio();

        historialNotas.add(new HistorialNota(estudiante, this, profesor, promedioAnterior, promedioNuevo, motivoCambio));
        return true;
    }

    public void limpiarRegistrosNotas() {
        registrosNotas.clear();
        historialNotas.clear();
    }

    private void validarEstudianteInscrito(Estudiante estudiante) {
        if (!estaInscrito(estudiante)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en esta materia.");
        }
    }

    public RegistroNota obtenerRegistroPorEstudiante(Estudiante estudiante) {
        for (RegistroNota registro : registrosNotas) {
            if (registro.getEstudiante().equals(estudiante)) {
                return registro;
            }
        }
        return null;
    }

    public float calcularPromedioMateria() {
        if (registrosNotas.isEmpty()) {
            return 0f;
        }
        float suma = 0;
        for (RegistroNota registro : registrosNotas) {
            suma += registro.calcularPromedio();
        }
        return suma / registrosNotas.size();
    }

    public int contarAprobados() {
        int total = 0;
        for (RegistroNota registro : registrosNotas) {
            if (registro.calcularPromedio() >= 14) {
                total++;
            }
        }
        return total;
    }

    public int contarRecuperacion() {
        int total = 0;
        for (RegistroNota registro : registrosNotas) {
            float promedio = registro.calcularPromedio();
            if (promedio >= 9 && promedio < 14) {
                total++;
            }
        }
        return total;
    }

    public int contarReprobados() {
        int total = 0;
        for (RegistroNota registro : registrosNotas) {
            if (registro.calcularPromedio() < 9) {
                total++;
            }
        }
        return total;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public String getParalelo() {
        return paralelo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public List<Estudiante> getEstudiantes() {
        return Collections.unmodifiableList(estudiantes);
    }

    public List<RegistroNota> getRegistrosNotas() {
        return Collections.unmodifiableList(registrosNotas);
    }

    public List<HistorialNota> getHistorialNotas() {
        return Collections.unmodifiableList(historialNotas);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombreMateria + " [" + paralelo + "]";
    }
}
