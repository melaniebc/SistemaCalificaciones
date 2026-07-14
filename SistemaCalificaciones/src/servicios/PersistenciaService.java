package servicios;

import academico.Carrera;
import academico.Materia;
import usuarios.Estudiante;
import usuarios.Profesor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class PersistenciaService {

    public abstract void guardarCarreras(Path ruta, List<Carrera> carreras) throws IOException;

    public abstract List<Carrera> cargarCarreras(Path ruta) throws IOException;

    public abstract void guardarProfesores(Path ruta, List<Profesor> profesores) throws IOException;

    public abstract List<Profesor> cargarProfesores(Path ruta) throws IOException;

    public abstract void guardarEstudiantes(Path ruta, List<Estudiante> estudiantes) throws IOException;

    public abstract List<Estudiante> cargarEstudiantes(Path ruta, List<Carrera> carrerasDisponibles) throws IOException;

    public abstract void guardarMaterias(Path ruta, List<Materia> materias) throws IOException;

    public abstract List<Materia> cargarMaterias(Path ruta, List<Carrera> carrerasDisponibles, List<Profesor> profesoresDisponibles) throws IOException;

    public abstract void guardarNotas(Path archivo, List<Materia> materias) throws IOException;

    public abstract void cargarNotasSiExiste(Path archivo, List<Materia> materias, List<Estudiante> estudiantes, List<Profesor> profesores) throws IOException;
}
