package academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carrera {
    private String codigo;
    private String nombre;
    private List<Materia> materias;

    public Carrera(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.materias = new ArrayList<>();
    }

    public void agregarMateria(Materia materia) {
        if (materia != null && !materias.contains(materia)) {
            materias.add(materia);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Materia> getMaterias() {
        return Collections.unmodifiableList(materias);
    }

    @Override
    public String toString() {
        return "(" + codigo + ") " + nombre;
    }
}
