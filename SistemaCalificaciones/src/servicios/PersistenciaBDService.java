package servicios;

import academico.Carrera;
import academico.Materia;
import evaluacion.RegistroNota;
import usuarios.Administrador;
import usuarios.Estudiante;
import usuarios.Profesor;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaBDService extends PersistenciaService {

    @Override
    public void guardarCarreras(Path ruta, List<Carrera> carreras) throws IOException {
        String sql = """
                INSERT INTO carreras (codigo, nombre)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE nombre = VALUES(nombre)
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            for (Carrera c : carreras) {
                ps.setString(1, c.getCodigo());
                ps.setString(2, c.getNombre());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException ex) {
            throw new IOException("Error guardando carreras en la base de datos", ex);
        }
    }

    @Override
    public List<Carrera> cargarCarreras(Path ruta) throws IOException {
        List<Carrera> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre FROM carreras ORDER BY nombre";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Carrera(rs.getString("codigo"), rs.getString("nombre")));
            }
        } catch (SQLException ex) {
            throw new IOException("Error cargando carreras desde la base de datos", ex);
        }
        return lista;
    }

    @Override
    public void guardarProfesores(Path ruta, List<Profesor> profesores) throws IOException {
        String sqlUsuario = """
                INSERT INTO usuarios (id, nombre, correo, password, rol)
                VALUES (?, ?, ?, ?, 'PROFESOR')
                ON DUPLICATE KEY UPDATE
                    nombre = VALUES(nombre), correo = VALUES(correo), password = VALUES(password), rol = VALUES(rol)
                """;
        String sqlProfesor = """
                INSERT INTO profesores (usuario_id, departamento)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE departamento = VALUES(departamento)
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement psUsuario = cn.prepareStatement(sqlUsuario);
             PreparedStatement psProfesor = cn.prepareStatement(sqlProfesor)) {
            cn.setAutoCommit(false);
            for (Profesor p : profesores) {
                psUsuario.setString(1, p.getId());
                psUsuario.setString(2, p.getNombre());
                psUsuario.setString(3, p.getCorreo());
                psUsuario.setString(4, p.getPassword());
                psUsuario.addBatch();

                psProfesor.setString(1, p.getId());
                psProfesor.setString(2, p.getDepartamento());
                psProfesor.addBatch();
            }
            psUsuario.executeBatch();
            psProfesor.executeBatch();
            cn.commit();
        } catch (SQLException ex) {
            throw new IOException("Error guardando profesores en la base de datos", ex);
        }
    }

    @Override
    public List<Profesor> cargarProfesores(Path ruta) throws IOException {
        List<Profesor> lista = new ArrayList<>();
        String sql = """
                SELECT u.id, u.nombre, u.correo, u.password, p.departamento
                FROM profesores p
                INNER JOIN usuarios u ON u.id = p.usuario_id
                ORDER BY u.nombre
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Profesor(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("departamento")
                ));
            }
        } catch (SQLException ex) {
            throw new IOException("Error cargando profesores desde la base de datos", ex);
        }
        return lista;
    }

    @Override
    public void guardarEstudiantes(Path ruta, List<Estudiante> estudiantes) throws IOException {
        String sqlUsuario = """
                INSERT INTO usuarios (id, nombre, correo, password, rol)
                VALUES (?, ?, ?, ?, 'ESTUDIANTE')
                ON DUPLICATE KEY UPDATE
                    nombre = VALUES(nombre), correo = VALUES(correo), password = VALUES(password), rol = VALUES(rol)
                """;
        String sqlEstudiante = """
                INSERT INTO estudiantes (usuario_id, carrera_codigo, nivel)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE carrera_codigo = VALUES(carrera_codigo), nivel = VALUES(nivel)
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement psUsuario = cn.prepareStatement(sqlUsuario);
             PreparedStatement psEstudiante = cn.prepareStatement(sqlEstudiante)) {
            cn.setAutoCommit(false);
            for (Estudiante e : estudiantes) {
                psUsuario.setString(1, e.getId());
                psUsuario.setString(2, e.getNombre());
                psUsuario.setString(3, e.getCorreo());
                psUsuario.setString(4, e.getPassword());
                psUsuario.addBatch();

                psEstudiante.setString(1, e.getId());
                psEstudiante.setString(2, e.getCarrera().getCodigo());
                psEstudiante.setInt(3, e.getNivel());
                psEstudiante.addBatch();
            }
            psUsuario.executeBatch();
            psEstudiante.executeBatch();
            cn.commit();
        } catch (SQLException ex) {
            throw new IOException("Error guardando estudiantes en la base de datos", ex);
        }
    }

    @Override
    public List<Estudiante> cargarEstudiantes(Path ruta, List<Carrera> carrerasDisponibles) throws IOException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = """
                SELECT u.id, u.nombre, u.correo, u.password, e.carrera_codigo, e.nivel
                FROM estudiantes e
                INNER JOIN usuarios u ON u.id = e.usuario_id
                ORDER BY u.nombre
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Carrera carrera = buscarCarrera(carrerasDisponibles, rs.getString("carrera_codigo"));
                if (carrera != null) {
                    lista.add(new Estudiante(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("password"),
                            carrera,
                            rs.getInt("nivel")
                    ));
                }
            }
        } catch (SQLException ex) {
            throw new IOException("Error cargando estudiantes desde la base de datos", ex);
        }
        return lista;
    }

    @Override
    public void guardarMaterias(Path ruta, List<Materia> materias) throws IOException {
        String sql = """
                INSERT INTO materias (codigo, nombre, paralelo, periodo, carrera_codigo, profesor_id)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    nombre = VALUES(nombre), paralelo = VALUES(paralelo), periodo = VALUES(periodo),
                    carrera_codigo = VALUES(carrera_codigo), profesor_id = VALUES(profesor_id)
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            for (Materia m : materias) {
                ps.setString(1, m.getCodigo());
                ps.setString(2, m.getNombreMateria());
                ps.setString(3, m.getParalelo());
                ps.setString(4, m.getPeriodo());
                ps.setString(5, m.getCarrera().getCodigo());
                ps.setString(6, m.getProfesor().getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException ex) {
            throw new IOException("Error guardando materias en la base de datos", ex);
        }
    }

    @Override
    public List<Materia> cargarMaterias(Path ruta, List<Carrera> carrerasDisponibles, List<Profesor> profesoresDisponibles) throws IOException {
        List<Materia> lista = new ArrayList<>();
        String sql = """
                SELECT codigo, nombre, paralelo, periodo, carrera_codigo, profesor_id
                FROM materias
                ORDER BY periodo, nombre
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Carrera carrera = buscarCarrera(carrerasDisponibles, rs.getString("carrera_codigo"));
                Profesor profesor = buscarProfesor(profesoresDisponibles, rs.getString("profesor_id"));
                if (carrera != null && profesor != null) {
                    lista.add(new Materia(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("paralelo"),
                            rs.getString("periodo"),
                            carrera,
                            profesor
                    ));
                }
            }
        } catch (SQLException ex) {
            throw new IOException("Error cargando materias desde la base de datos", ex);
        }
        return lista;
    }

    @Override
    public void guardarNotas(Path archivo, List<Materia> materias) throws IOException {
        String sqlMatricula = """
                INSERT INTO materia_estudiante (materia_codigo, estudiante_id)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE estudiante_id = VALUES(estudiante_id)
                """;
        String sqlNota = """
                INSERT INTO registros_notas (materia_codigo, estudiante_id, profesor_id, nota1, nota2, nota3, observacion)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    profesor_id = VALUES(profesor_id), nota1 = VALUES(nota1), nota2 = VALUES(nota2),
                    nota3 = VALUES(nota3), observacion = VALUES(observacion)
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement psMatricula = cn.prepareStatement(sqlMatricula);
             PreparedStatement psNota = cn.prepareStatement(sqlNota)) {
            cn.setAutoCommit(false);

            for (Materia m : materias) {
                for (Estudiante e : m.getEstudiantes()) {
                    psMatricula.setString(1, m.getCodigo());
                    psMatricula.setString(2, e.getId());
                    psMatricula.addBatch();
                }

                for (RegistroNota r : m.getRegistrosNotas()) {
                    psMatricula.setString(1, m.getCodigo());
                    psMatricula.setString(2, r.getEstudiante().getId());
                    psMatricula.addBatch();

                    psNota.setString(1, m.getCodigo());
                    psNota.setString(2, r.getEstudiante().getId());
                    psNota.setString(3, r.getProfesor().getId());
                    psNota.setFloat(4, r.getNota1());
                    psNota.setFloat(5, r.getNota2());
                    psNota.setFloat(6, r.getNota3());
                    psNota.setString(7, r.getObservacion());
                    psNota.addBatch();
                }
            }

            psMatricula.executeBatch();
            psNota.executeBatch();
            cn.commit();
        } catch (SQLException ex) {
            throw new IOException("Error guardando notas en la base de datos", ex);
        }
    }

    @Override
    public void cargarNotasSiExiste(Path archivo, List<Materia> materias, List<Estudiante> estudiantes, List<Profesor> profesores) throws IOException {
        for (Materia materia : materias) {
            materia.limpiarRegistrosNotas();
        }

        cargarMatriculas(materias, estudiantes);

        String sql = """
                SELECT materia_codigo, estudiante_id, profesor_id, nota1, nota2, nota3, observacion
                FROM registros_notas
                ORDER BY materia_codigo, estudiante_id
                """;
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Materia materia = buscarMateria(materias, rs.getString("materia_codigo"));
                Estudiante estudiante = buscarEstudiante(estudiantes, rs.getString("estudiante_id"));
                Profesor profesor = buscarProfesor(profesores, rs.getString("profesor_id"));

                if (materia != null && estudiante != null && profesor != null) {
                    if (!materia.estaInscrito(estudiante)) {
                        materia.inscribirEstudiante(estudiante);
                    }
                    if (materia.obtenerRegistroPorEstudiante(estudiante) == null) {
                        materia.registrarNotas(
                                estudiante,
                                profesor,
                                rs.getFloat("nota1"),
                                rs.getFloat("nota2"),
                                rs.getFloat("nota3"),
                                rs.getString("observacion")
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            throw new IOException("Error cargando notas desde la base de datos", ex);
        }
    }

    private void cargarMatriculas(List<Materia> materias, List<Estudiante> estudiantes) throws IOException {
        String sql = "SELECT materia_codigo, estudiante_id FROM materia_estudiante";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Materia materia = buscarMateria(materias, rs.getString("materia_codigo"));
                Estudiante estudiante = buscarEstudiante(estudiantes, rs.getString("estudiante_id"));
                if (materia != null && estudiante != null && !materia.estaInscrito(estudiante)) {
                    materia.inscribirEstudiante(estudiante);
                }
            }
        } catch (SQLException ex) {
            throw new IOException("Error cargando matrículas desde la base de datos", ex);
        }
    }
    public List<Administrador> cargarAdministradores() throws IOException {
        List<Administrador> lista = new ArrayList<>();

        String sql = """
            SELECT
                u.id,
                u.nombre,
                u.correo,
                u.password,
                a.area_responsable
            FROM administradores a
            INNER JOIN usuarios u
                ON u.id = a.usuario_id
            WHERE u.rol = 'ADMINISTRADOR'
            ORDER BY u.nombre
            """;

        try (
                Connection cn = ConexionBD.obtenerConexion();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Administrador administrador = new Administrador(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("area_responsable")
                );

                lista.add(administrador);
            }

        } catch (SQLException ex) {
            throw new IOException(
                    "Error cargando administradores desde la base de datos",
                    ex
            );
        }

        return lista;
    }
    private Carrera buscarCarrera(List<Carrera> carreras, String codigo) {
        for (Carrera carrera : carreras) {
            if (carrera.getCodigo().equals(codigo)) {
                return carrera;
            }
        }
        return null;
    }

    private Materia buscarMateria(List<Materia> materias, String codigo) {
        for (Materia materia : materias) {
            if (materia.getCodigo().equals(codigo)) {
                return materia;
            }
        }
        return null;
    }

    private Estudiante buscarEstudiante(List<Estudiante> estudiantes, String id) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getId().equals(id)) {
                return estudiante;
            }
        }
        return null;
    }

    private Profesor buscarProfesor(List<Profesor> profesores, String id) {
        for (Profesor profesor : profesores) {
            if (profesor.getId().equals(id)) {
                return profesor;
            }
        }
        return null;
    }
}
