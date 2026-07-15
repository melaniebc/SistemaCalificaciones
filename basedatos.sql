-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-07-2026 a las 01:55:49
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sistema_calificaciones`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administradores`
--

CREATE TABLE `administradores` (
  `usuario_id` varchar(20) NOT NULL,
  `area_responsable` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `administradores`
--

INSERT INTO `administradores` (`usuario_id`, `area_responsable`) VALUES
('ADM001', 'Gestión Académica');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carreras`
--

CREATE TABLE `carreras` (
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `carreras`
--

INSERT INTO `carreras` (`codigo`, `nombre`) VALUES
('RRA20', 'Ciencia de Datos e IA'),
('SIS01', 'Ingeniería en Sistemas');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estudiantes`
--

CREATE TABLE `estudiantes` (
  `usuario_id` varchar(20) NOT NULL,
  `carrera_codigo` varchar(20) NOT NULL,
  `nivel` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `estudiantes`
--

INSERT INTO `estudiantes` (`usuario_id`, `carrera_codigo`, `nivel`) VALUES
('202510910', 'RRA20', 2),
('202511288', 'RRA20', 1),
('202511398', 'SIS01', 3),
('202511591', 'RRA20', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materias`
--

CREATE TABLE `materias` (
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `nivel` tinyint(4) DEFAULT NULL,
  `creditos` tinyint(4) DEFAULT NULL,
  `horas` smallint(6) DEFAULT NULL,
  `pensum` smallint(6) DEFAULT NULL,
  `paralelo` varchar(20) NOT NULL,
  `periodo` varchar(50) NOT NULL,
  `carrera_codigo` varchar(20) NOT NULL,
  `profesor_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `materias`
--

INSERT INTO `materias` (`codigo`, `nombre`, `nivel`, `creditos`, `horas`, `pensum`, `paralelo`, `periodo`, `carrera_codigo`, `profesor_id`) VALUES
('ICCD144', 'Programacion I', NULL, NULL, NULL, NULL, 'GR4CD', '2026-A', 'RRA20', '202211534'),
('ICCD323', 'Sistemas Operativos', NULL, NULL, NULL, NULL, 'GR6SF', '2026-A', 'SIS01', '202078356'),
('MATD113', 'Algebra Lineal', NULL, NULL, NULL, NULL, 'GR1', '2026-A', 'RRA20', '202211534'),
('MATD123', 'Calculo en una Variable', NULL, NULL, NULL, NULL, 'GR2CD', '2026-A', 'RRA20', '202211534');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materia_estudiante`
--

CREATE TABLE `materia_estudiante` (
  `materia_codigo` varchar(20) NOT NULL,
  `estudiante_id` varchar(20) NOT NULL,
  `fecha_inscripcion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `materia_estudiante`
--

INSERT INTO `materia_estudiante` (`materia_codigo`, `estudiante_id`, `fecha_inscripcion`) VALUES
('ICCD144', '202510910', '2026-07-14 23:01:23'),
('ICCD323', '202510910', '2026-07-14 22:38:29'),
('ICCD323', '202511398', '2026-07-14 22:46:06'),
('MATD113', '202510910', '2026-07-14 22:22:28'),
('MATD113', '202511288', '2026-07-14 22:22:38'),
('MATD123', '202510910', '2026-07-14 22:43:15'),
('MATD123', '202511591', '2026-07-14 22:34:27');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesores`
--

CREATE TABLE `profesores` (
  `usuario_id` varchar(20) NOT NULL,
  `departamento` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `profesores`
--

INSERT INTO `profesores` (`usuario_id`, `departamento`) VALUES
('202078356', 'Sistemas'),
('202211534', 'Sistemas');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registros_notas`
--

CREATE TABLE `registros_notas` (
  `id` int(11) NOT NULL,
  `materia_codigo` varchar(20) NOT NULL,
  `estudiante_id` varchar(20) NOT NULL,
  `profesor_id` varchar(20) NOT NULL,
  `nota1` decimal(5,2) NOT NULL DEFAULT 0.00,
  `nota2` decimal(5,2) NOT NULL DEFAULT 0.00,
  `nota3` decimal(5,2) NOT NULL DEFAULT 0.00,
  `observacion` varchar(255) NOT NULL DEFAULT 'Sin observación',
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_ultima_modificacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `registros_notas`
--

INSERT INTO `registros_notas` (`id`, `materia_codigo`, `estudiante_id`, `profesor_id`, `nota1`, `nota2`, `nota3`, `observacion`, `fecha_registro`, `fecha_ultima_modificacion`) VALUES
(1, 'MATD113', '202510910', '202211534', 19.04, 10.00, 9.00, 'Inscripción inicial', '2026-07-14 22:22:28', '2026-07-14 22:24:02'),
(3, 'MATD113', '202511288', '202211534', 14.04, 13.92, 16.00, 'Inscripción inicial', '2026-07-14 22:22:38', '2026-07-14 22:23:36'),
(12, 'MATD123', '202511591', '202211534', 12.00, 20.00, 8.00, 'Inscripción inicial', '2026-07-14 22:34:27', '2026-07-14 22:35:18'),
(19, 'ICCD323', '202510910', '202078356', 18.90, 15.50, 13.00, 'Inscripción inicial', '2026-07-14 22:38:29', '2026-07-14 22:39:37'),
(34, 'ICCD323', '202511398', '202078356', 15.98, 17.90, 4.00, 'Inscripción inicial', '2026-07-14 22:46:06', '2026-07-14 22:46:43'),
(55, 'ICCD144', '202510910', '202211534', 17.80, 13.90, 15.00, 'Sin observación', '2026-07-14 23:02:03', '2026-07-14 23:02:03');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` varchar(20) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `correo` varchar(120) NOT NULL,
  `password` varchar(100) NOT NULL,
  `rol` enum('ADMINISTRADOR','PROFESOR','ESTUDIANTE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `correo`, `password`, `rol`) VALUES
('202078356', 'Tiosbel Pepe Perez Ortega', 'tiosbel.perez@epn.edu.ec', 't123', 'PROFESOR'),
('202211534', 'Ivan Manuel Herrera Herrera', 'ivan.herrera@epn.edu.ec', 'h123', 'PROFESOR'),
('202510910', 'Stefano Alexander Cabezas Granda', 'stefano.cabezas@epn.edu.ec', 's123', 'ESTUDIANTE'),
('202511288', 'Zander Kein Castillo Sacon', 'zander.castillo@epn.edu.ec', 'z123', 'ESTUDIANTE'),
('202511398', 'Teo Jhamin Maita Quizpe', 'teo.maita@epn.edu.ec', 'tm123', 'ESTUDIANTE'),
('202511591', 'Viviana Mabell Guerrón Mogollon', 'viviana.guerron@epn.edu.ec', 'v123', 'ESTUDIANTE'),
('ADM001', 'Administrador General', 'admin@epn.edu.ec', 'admin', 'ADMINISTRADOR');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_reporte_notas`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_reporte_notas` (
`codigo_carrera` varchar(20)
,`carrera` varchar(150)
,`codigo_materia` varchar(20)
,`materia` varchar(150)
,`paralelo` varchar(20)
,`periodo` varchar(50)
,`estudiante_id` varchar(20)
,`estudiante` varchar(150)
,`profesor` varchar(150)
,`nota1` decimal(5,2)
,`nota2` decimal(5,2)
,`nota3` decimal(5,2)
,`promedio` decimal(8,2)
,`estado` varchar(12)
,`observacion` varchar(255)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_reporte_notas`
--
DROP TABLE IF EXISTS `vista_reporte_notas`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_reporte_notas`  AS SELECT `c`.`codigo` AS `codigo_carrera`, `c`.`nombre` AS `carrera`, `m`.`codigo` AS `codigo_materia`, `m`.`nombre` AS `materia`, `m`.`paralelo` AS `paralelo`, `m`.`periodo` AS `periodo`, `ue`.`id` AS `estudiante_id`, `ue`.`nombre` AS `estudiante`, `up`.`nombre` AS `profesor`, `rn`.`nota1` AS `nota1`, `rn`.`nota2` AS `nota2`, `rn`.`nota3` AS `nota3`, round((`rn`.`nota1` + `rn`.`nota2` + `rn`.`nota3`) / 3,2) AS `promedio`, CASE WHEN (`rn`.`nota1` + `rn`.`nota2` + `rn`.`nota3`) / 3 >= 14 THEN 'Aprobado' WHEN (`rn`.`nota1` + `rn`.`nota2` + `rn`.`nota3`) / 3 >= 9 THEN 'Recuperación' ELSE 'Reprobado' END AS `estado`, `rn`.`observacion` AS `observacion` FROM ((((`registros_notas` `rn` join `materias` `m` on(`m`.`codigo` = `rn`.`materia_codigo`)) join `carreras` `c` on(`c`.`codigo` = `m`.`carrera_codigo`)) join `usuarios` `ue` on(`ue`.`id` = `rn`.`estudiante_id`)) join `usuarios` `up` on(`up`.`id` = `rn`.`profesor_id`)) ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD PRIMARY KEY (`usuario_id`);

--
-- Indices de la tabla `carreras`
--
ALTER TABLE `carreras`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `estudiantes`
--
ALTER TABLE `estudiantes`
  ADD PRIMARY KEY (`usuario_id`),
  ADD KEY `fk_estudiante_carrera` (`carrera_codigo`);

--
-- Indices de la tabla `materias`
--
ALTER TABLE `materias`
  ADD PRIMARY KEY (`codigo`),
  ADD KEY `fk_materia_carrera` (`carrera_codigo`),
  ADD KEY `fk_materia_profesor` (`profesor_id`);

--
-- Indices de la tabla `materia_estudiante`
--
ALTER TABLE `materia_estudiante`
  ADD PRIMARY KEY (`materia_codigo`,`estudiante_id`),
  ADD KEY `fk_matricula_estudiante` (`estudiante_id`);

--
-- Indices de la tabla `profesores`
--
ALTER TABLE `profesores`
  ADD PRIMARY KEY (`usuario_id`);

--
-- Indices de la tabla `registros_notas`
--
ALTER TABLE `registros_notas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_nota_materia_estudiante` (`materia_codigo`,`estudiante_id`),
  ADD KEY `fk_nota_estudiante` (`estudiante_id`),
  ADD KEY `fk_nota_profesor` (`profesor_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `registros_notas`
--
ALTER TABLE `registros_notas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD CONSTRAINT `fk_admin_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `estudiantes`
--
ALTER TABLE `estudiantes`
  ADD CONSTRAINT `fk_estudiante_carrera` FOREIGN KEY (`carrera_codigo`) REFERENCES `carreras` (`codigo`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_estudiante_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `materias`
--
ALTER TABLE `materias`
  ADD CONSTRAINT `fk_materia_carrera` FOREIGN KEY (`carrera_codigo`) REFERENCES `carreras` (`codigo`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_materia_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `profesores` (`usuario_id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `materia_estudiante`
--
ALTER TABLE `materia_estudiante`
  ADD CONSTRAINT `fk_matricula_estudiante` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_matricula_materia` FOREIGN KEY (`materia_codigo`) REFERENCES `materias` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `profesores`
--
ALTER TABLE `profesores`
  ADD CONSTRAINT `fk_profesor_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `registros_notas`
--
ALTER TABLE `registros_notas`
  ADD CONSTRAINT `fk_nota_estudiante` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`usuario_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_nota_materia` FOREIGN KEY (`materia_codigo`) REFERENCES `materias` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_nota_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `profesores` (`usuario_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
