package usuarios;

import academico.Materia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Representa al profesor. Puede registrar y modificar calificaciones
//únicamente en las materias que tiene asignadas.

public class Profesor extends Usuario {
    private String departamento;
    private List<Materia> materiasAsignadas;

    public Profesor(String id, String nombre, String correo, String password, String departamento) {
        super(id, nombre, correo, password);
        this.departamento = departamento;
        this.materiasAsignadas = new ArrayList<>();
    }

    @Override
    public String obtenerPerfil() {
        return "Perfil Profesor - Departamento: " + departamento + " - " + getNombre();
    }

    public void asignarMateria(Materia materia) {
        if (materia != null && !materiasAsignadas.contains(materia)) {
            materiasAsignadas.add(materia);
        }
    }

    public boolean puedeGestionar(Materia materia) {
        return materia != null && materiasAsignadas.contains(materia);
    }

    public boolean registrarNotas(Materia materia, Estudiante estudiante, float nota1, float nota2, float nota3, String observacion) {
        validarPermisoSobreMateria(materia);
        return materia.registrarNotas(estudiante, this, nota1, nota2, nota3, observacion);
    }

    public boolean modificarNotas(Materia materia, Estudiante estudiante, float nota1, float nota2, float nota3,
                                  String observacion, String motivoCambio) {
        validarPermisoSobreMateria(materia);
        return materia.modificarNotas(estudiante, this, nota1, nota2, nota3, observacion, motivoCambio);
    }

    private void validarPermisoSobreMateria(Materia materia) {
        if (!puedeGestionar(materia)) {
            throw new IllegalArgumentException("El profesor no tiene permiso para gestionar esta materia.");
        }
    }

    public String getDepartamento() {
        return departamento;
    }

    public List<Materia> getMateriasAsignadas() {
        return Collections.unmodifiableList(materiasAsignadas);
    }
}
