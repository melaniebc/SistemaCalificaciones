-- ==========================================================
-- BASE DE DATOS: Sistema de Calificaciones
-- Motor recomendado: MySQL / MariaDB
-- ==========================================================

CREATE DATABASE IF NOT EXISTS sistema_calificaciones
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sistema_calificaciones;

-- Limpieza opcional para volver a crear desde cero.
-- Úsala solo si estás practicando y no tienes datos importantes.
-- SET FOREIGN_KEY_CHECKS = 0;
-- DROP TABLE IF EXISTS historial_notas;
-- DROP TABLE IF EXISTS registros_notas;
-- DROP TABLE IF EXISTS materia_estudiante;
-- DROP TABLE IF EXISTS materias;
-- DROP TABLE IF EXISTS estudiantes;
-- DROP TABLE IF EXISTS profesores;
-- DROP TABLE IF EXISTS administradores;
-- DROP TABLE IF EXISTS usuarios;
-- DROP TABLE IF EXISTS carreras;
-- SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS carreras (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
    id VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'PROFESOR', 'ESTUDIANTE') NOT NULL
);

CREATE TABLE IF NOT EXISTS administradores (
    usuario_id VARCHAR(20) PRIMARY KEY,
    area_responsable VARCHAR(100) NOT NULL,
    CONSTRAINT fk_admin_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS profesores (
    usuario_id VARCHAR(20) PRIMARY KEY,
    departamento VARCHAR(100) NOT NULL,
    CONSTRAINT fk_profesor_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS estudiantes (
    usuario_id VARCHAR(20) PRIMARY KEY,
    carrera_codigo VARCHAR(20) NOT NULL,
    nivel INT NOT NULL,
    CONSTRAINT chk_estudiante_nivel CHECK (nivel BETWEEN 1 AND 10),
    CONSTRAINT fk_estudiante_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_estudiante_carrera
        FOREIGN KEY (carrera_codigo) REFERENCES carreras(codigo)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS materias (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    paralelo VARCHAR(20) NOT NULL,
    periodo VARCHAR(50) NOT NULL,
    carrera_codigo VARCHAR(20) NOT NULL,
    profesor_id VARCHAR(20) NOT NULL,
    CONSTRAINT fk_materia_carrera
        FOREIGN KEY (carrera_codigo) REFERENCES carreras(codigo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_materia_profesor
        FOREIGN KEY (profesor_id) REFERENCES profesores(usuario_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS materia_estudiante (
    materia_codigo VARCHAR(20) NOT NULL,
    estudiante_id VARCHAR(20) NOT NULL,
    fecha_inscripcion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (materia_codigo, estudiante_id),
    CONSTRAINT fk_matricula_materia
        FOREIGN KEY (materia_codigo) REFERENCES materias(codigo)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_matricula_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiantes(usuario_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS registros_notas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    materia_codigo VARCHAR(20) NOT NULL,
    estudiante_id VARCHAR(20) NOT NULL,
    profesor_id VARCHAR(20) NOT NULL,
    nota1 DECIMAL(5,2) NOT NULL DEFAULT 0,
    nota2 DECIMAL(5,2) NOT NULL DEFAULT 0,
    nota3 DECIMAL(5,2) NOT NULL DEFAULT 0,
    observacion VARCHAR(255) NOT NULL DEFAULT 'Sin observación',
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_ultima_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_nota1 CHECK (nota1 BETWEEN 0 AND 20),
    CONSTRAINT chk_nota2 CHECK (nota2 BETWEEN 0 AND 20),
    CONSTRAINT chk_nota3 CHECK (nota3 BETWEEN 0 AND 20),
    CONSTRAINT uq_nota_materia_estudiante UNIQUE (materia_codigo, estudiante_id),
    CONSTRAINT fk_nota_materia
        FOREIGN KEY (materia_codigo) REFERENCES materias(codigo)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_nota_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiantes(usuario_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_nota_profesor
        FOREIGN KEY (profesor_id) REFERENCES profesores(usuario_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS historial_notas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    materia_codigo VARCHAR(20) NOT NULL,
    estudiante_id VARCHAR(20) NOT NULL,
    profesor_id VARCHAR(20) NOT NULL,
    nota_anterior DECIMAL(5,2) NOT NULL,
    nota_nueva DECIMAL(5,2) NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_historial_materia
        FOREIGN KEY (materia_codigo) REFERENCES materias(codigo)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_historial_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiantes(usuario_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_historial_profesor
        FOREIGN KEY (profesor_id) REFERENCES profesores(usuario_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE OR REPLACE VIEW vista_reporte_notas AS
SELECT
    c.codigo AS codigo_carrera,
    c.nombre AS carrera,
    m.codigo AS codigo_materia,
    m.nombre AS materia,
    m.paralelo,
    m.periodo,
    ue.id AS estudiante_id,
    ue.nombre AS estudiante,
    up.nombre AS profesor,
    rn.nota1,
    rn.nota2,
    rn.nota3,
    ROUND((rn.nota1 + rn.nota2 + rn.nota3) / 3, 2) AS promedio,
    CASE
        WHEN ((rn.nota1 + rn.nota2 + rn.nota3) / 3) >= 14 THEN 'Aprobado'
        WHEN ((rn.nota1 + rn.nota2 + rn.nota3) / 3) >= 9 THEN 'Recuperación'
        ELSE 'Reprobado'
    END AS estado,
    rn.observacion
FROM registros_notas rn
INNER JOIN materias m ON m.codigo = rn.materia_codigo
INNER JOIN carreras c ON c.codigo = m.carrera_codigo
INNER JOIN usuarios ue ON ue.id = rn.estudiante_id
INNER JOIN usuarios up ON up.id = rn.profesor_id;

-- Datos mínimos por defecto. El administrador también está creado en el código Java.
INSERT INTO carreras (codigo, nombre) VALUES
('RRA20', 'Ciencia de Datos e Inteligencia Artificial'),
('SIS01', 'Ingeniería en Sistemas')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO usuarios (id, nombre, correo, password, rol) VALUES
('A01', 'Administrador Académico', 'admin@epn.edu.ec', 'admin', 'ADMINISTRADOR')
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    correo = VALUES(correo),
    password = VALUES(password),
    rol = VALUES(rol);

INSERT INTO administradores (usuario_id, area_responsable) VALUES
('A01', 'Gestión Académica')
ON DUPLICATE KEY UPDATE area_responsable = VALUES(area_responsable);
