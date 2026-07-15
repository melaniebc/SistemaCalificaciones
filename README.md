# 🚀 SAGA: Sistema Avanzado de Gestión Académica

**SAGA** es una plataforma integral diseñada para la administración académica universitaria, permitiendo una gestión centralizada y eficiente de usuarios, carreras, materias y el control riguroso de calificaciones.

## 📋 Descripción del Proyecto
Este sistema soluciona la fragmentación de la información académica mediante un entorno centralizado que facilita la interacción entre tres roles clave: **Administradores**, **Profesores** y **Estudiantes**. El proyecto destaca por su arquitectura robusta, escalable y modular, migrada exitosamente desde un sistema basado en archivos planos a una base de datos relacional.

## 🏗️ Arquitectura del Sistema
El proyecto sigue una arquitectura en capas que separa claramente la lógica de negocio de la presentación:

* **`app`**: Interfaz gráfica (GUI) y flujo de navegación.
* **`academico`**: Entidades fundamentales (Materias, Carreras).
* **`evaluacion`**: Lógica de notas, historial y criterios de evaluación.
* **`usuarios`**: Modelo de actores del sistema (con herencia y polimorfismo).
* **`servicios`**: Lógica de persistencia (JDBC/MySQL) y servicios de negocio.
* **`util`**: Utilitarios visuales y generadores de reportes PDF nativos.



## 🛠️ Características Principales
* **Gestión de Usuarios:** Implementación de jerarquías de clase con herencia y polimorfismo.
* **Control de Calificaciones:** Registro y modificación de notas con trazabilidad y auditoría completa de cambios.
* **Reportes Académicos:** Generador nativo de documentos PDF para reportes personales de estudiantes.
* **Persistencia Robusta:** Migración a base de datos relacional (**MySQL**) garantizando integridad referencial.
* **Interfaz Moderna:** Diseño personalizado y limpio utilizando Java Swing con gestión de estados mediante `CardLayout`.

## 💻 Tecnologías Utilizadas
* **Lenguaje:** Java (Paradigma Orientado a Objetos).
* **Base de Datos:** MySQL.
* **Interfaz Gráfica:** Java Swing (con personalización de componentes).
* **Reportes:** Generador de PDF construido a bajo nivel (nativo).
* **IDE:** IntelliJ IDEA.

## ⚙️ Configuración y Ejecución
1. **Base de Datos:** Importa el esquema SQL (`sistema_calificaciones`) en tu servidor MySQL.
2. **Dependencias:** Asegúrate de tener el driver `mysql-connector-j` en el *classpath* de tu proyecto.
3. **Conexión:** Configura tus credenciales en `servicios/ConexionBD.java`.
4. **Ejecución:** Ejecuta el método `main` en la clase `app/SistemaCalificacionesGUI`.


## 👥 Autores
Trabajo práctico desarrollado para la materia de Programación II (GR1CD) - Escuela Politécnica Nacional:

Melanie A. Buenaño - melanie.buenano@epn.edu.ec

Stefano A. Cabezas - stefano.cabezas@epn.edu.ec

Zander K. Castillo - zander.castillo@epn.edu.ec

Viviana M. Guerrón - viviana.guerron@epn.edu.ec

