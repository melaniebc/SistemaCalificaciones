package usuarios;

import academico.Materia;

import java.util.List;


//Representa al administrador académico.

public class Administrador extends Usuario {
    private String areaResponsable;

    public Administrador(String id, String nombre, String correo, String password, String areaResponsable) {
        super(id, nombre, correo, password);
        this.areaResponsable = areaResponsable;
    }

    @Override
    public String obtenerPerfil() {
        return "Perfil Administrador - Área: " + areaResponsable + " - " + getNombre();
    }

    public void registrarEstudiante(List<Estudiante> estudiantes, Estudiante estudiante) {
        if (estudiante != null && !estudiantes.contains(estudiante)) {
            estudiantes.add(estudiante);
        }
    }

    public void asignarEstudianteAMateria(Materia materia, Estudiante estudiante) {
        if (materia != null && estudiante != null) {
            materia.inscribirEstudiante(estudiante);
        }
    }

    public String getAreaResponsable() {
        return areaResponsable;
    }
}
