package usuarios;

import academico.Carrera;
import academico.Materia;
import evaluacion.RegistroNota;


//Representa al estudiante. El estudiante solo consulta sus calificaciones.

public class Estudiante extends Usuario {
    private Carrera carrera;
    private int nivel;

    public Estudiante(String id, String nombre, String correo, String password, Carrera carrera, int nivel) {
        super(id, nombre, correo, password);
        this.carrera = carrera;
        this.nivel = nivel;
    }

    @Override
    public String obtenerPerfil() {
        return "Perfil Estudiante - Nivel " + nivel + " - Carrera: " + carrera.getNombre() + " - " + getNombre();
    }

    public String verNota(Materia materia) {
        if (materia == null) {
            return "Seleccione una materia para consultar sus notas.";
        }
        if (!materia.estaInscrito(this)) {
            return "El estudiante " + getNombre() + " no está inscrito en " + materia.getNombreMateria() + ".";
        }

        RegistroNota registro = materia.obtenerRegistroPorEstudiante(this);
        if (registro == null) {
            return "Estudiante: " + getNombre() + "\n"
                    + "Carrera: " + carrera.getNombre() + "\n"
                    + "Materia: " + materia.getNombreMateria() + "\n"
                    + "Estado: todavía no existen calificaciones registradas.";
        }

        return "Estudiante: " + getNombre() + "\n"
                + "Carrera: " + carrera.getNombre() + "\n"
                + "Materia: " + materia.getNombreMateria() + "\n"
                + "Profesor: " + materia.getProfesor().getNombre() + "\n"
                + "Promedio: " + String.format("%.2f", registro.calcularPromedio()) + " / 20\n"
                + "Estado: " + registro.calcularEstado(registro.calcularPromedio()) + "\n"
                + "Observación: " + registro.getObservacion();
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public int getNivel() {
        return nivel;
    }
}
