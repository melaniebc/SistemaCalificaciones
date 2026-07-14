package app;

import academico.Carrera;
import academico.Materia;
import evaluacion.HistorialNota;
import evaluacion.RegistroNota;
import usuarios.Administrador;
import usuarios.Estudiante;
import usuarios.Profesor;
import usuarios.Usuario;
import servicios.CalificacionService;
import servicios.PersistenciaBDService;
import servicios.ReporteService;
import util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


 //Interfaz gráfica principal del sistema SAGA.

public class SistemaCalificacionesGUI extends JFrame {

    private CardLayout layoutPrincipal;
    private JPanel contenedorPrincipal;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<Profesor> profesores = new ArrayList<>();
    private final List<Carrera> carreras = new ArrayList<>();
    private final List<Materia> materias = new ArrayList<>();

    private Administrador administrador;
    private Usuario usuarioActual;

    private final CalificacionService calificacionService = new CalificacionService();
    private final PersistenciaBDService persistenciaService = new PersistenciaBDService();
    private final ReporteService reporteService = new ReporteService();
    private static final Path ARCHIVO_PERSISTENCIA = Paths.get("data", "notas.csv");

    // Login
    private JComboBox<String> comboRolLogin;
    private JTextField txtCorreoLogin;
    private JPasswordField txtPasswordLogin;

    // Profesor
    private JLabel lblNombreProfesor;
    private JLabel lblUsuarioBarraProfesor;
    private JLabel lblResumenMateriasProfesor;
    private JLabel lblResumenEstudiantesProfesor;
    private JLabel lblResumenPromedioProfesor;
    private JLabel lblResumenAprobadosProfesor;
    private CardLayout layoutProfesorContenido;
    private JPanel panelContenidoProfesor;
    private JComboBox<Carrera> comboCarreraProfesor;
    private JComboBox<String> comboPeriodoProfesor;
    private JComboBox<Materia> comboMateriaProfesor;
    private JComboBox<Estudiante> comboEstudianteProfesor;
    private JTextField txtNota1;
    private JTextField txtNota2;
    private JTextField txtNota3;
    private JTextField txtObservacion;
    private JTextField txtMotivoCambio;
    private DefaultTableModel modeloTablaProfesor;
    private DefaultTableModel modeloTablaEstudiantesProfesor;
    private DefaultTableModel modeloTablaMateriasProfesor;
    private DefaultTableModel modeloTablaHistorialProfesor;

    // Estudiante
    private JLabel lblNombreEstudiante;
    private JLabel lblUsuarioBarraEstudiante;
    private JLabel lblCarreraEstudiante;
    private JLabel lblPromedioEstudiante;
    private JLabel lblEstadoEstudiante;
    private JLabel lblMateriasEstudiante;
    private CardLayout layoutEstudianteContenido;
    private JPanel panelContenidoEstudiante;
    private JComboBox<Materia> comboMateriaEstudiante;
    private JTextArea areaResumenEstudiante;
    private DefaultTableModel modeloTablaEstudiante;
    private DefaultTableModel modeloTablaMateriasEstudiante;

    private JTextField txtCodigoCarreraForm;
    private JTextField txtNombreCarreraForm;

    public SistemaCalificacionesGUI() {
        cargarDatosIniciales();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("SAGA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1260, 760);
        setMinimumSize(new Dimension(1080, 660));
        setLocationRelativeTo(null);

        layoutPrincipal = new CardLayout();
        contenedorPrincipal = new JPanel(layoutPrincipal);
        contenedorPrincipal.add(crearPanelLogin(), "LOGIN");
        contenedorPrincipal.add(crearPanelProfesor(), "PROFESOR");
        contenedorPrincipal.add(crearPanelEstudiante(), "ESTUDIANTE");
        contenedorPrincipal.add(crearPanelAdministrador(), "ADMINISTRADOR");
        add(contenedorPrincipal);
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.FONDO);
        panel.add(crearBarraSuperior("MÓDULO:\nACADÉMICO", "INVITADO"), BorderLayout.NORTH);
        panel.add(crearPiePagina(), BorderLayout.SOUTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(UIUtils.FONDO);

        JPanel tarjeta = UIUtils.crearTarjeta();
        tarjeta.setPreferredSize(new Dimension(450, 400));
        tarjeta.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Aumentamos un poco el margen superior/inferior
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel avatar = new JLabel("●", SwingConstants.CENTER);
        avatar.setFont(new Font("Arial", Font.BOLD, 44));
        avatar.setForeground(new Color(91, 103, 132));
        tarjeta.add(avatar, gbc);

        gbc.gridy++;
        JLabel titulo = new JLabel("Bienvenido a SAGA", SwingConstants.CENTER);
        titulo.setFont(UIUtils.FUENTE_TITULO);
        tarjeta.add(titulo, gbc);

        gbc.gridy++;
        comboRolLogin = new JComboBox<>(new String[]{"ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"});
        UIUtils.estilizarCombo(comboRolLogin);
        tarjeta.add(comboRolLogin, gbc);

        gbc.gridy++;
        txtCorreoLogin = new JTextField();
        txtCorreoLogin.setToolTipText("Correo institucional");
        UIUtils.estilizarCampo(txtCorreoLogin);
        tarjeta.add(txtCorreoLogin, gbc);

        gbc.gridy++;
        txtPasswordLogin = new JPasswordField();
        txtPasswordLogin.setToolTipText("Contraseña");
        UIUtils.estilizarCampo(txtPasswordLogin);
        tarjeta.add(txtPasswordLogin, gbc);

        gbc.gridy++;
        JButton btnIngresar = UIUtils.crearBotonPrincipal("Ingresar");
        gbc.insets = new Insets(20, 18, 7, 18);
        tarjeta.add(btnIngresar, gbc);

        btnIngresar.addActionListener(e -> iniciarSesion());

        centro.add(tarjeta);
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelProfesor() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.FONDO);

        lblNombreProfesor = new JLabel("PROFESOR");
        lblUsuarioBarraProfesor = new JLabel("PROFESOR", SwingConstants.RIGHT);
        panel.add(crearBarraSuperiorUsuario("", lblUsuarioBarraProfesor), BorderLayout.NORTH);
        panel.add(crearMenuLateralProfesor(), BorderLayout.WEST);

        layoutProfesorContenido = new CardLayout();
        panelContenidoProfesor = new JPanel(layoutProfesorContenido);
        panelContenidoProfesor.setBackground(UIUtils.FONDO);
        panelContenidoProfesor.add(crearInicioProfesor(), "INICIO");
        panelContenidoProfesor.add(crearRegistroCalificacionesProfesor(), "REGISTRO");
        panelContenidoProfesor.add(crearListaEstudiantesProfesor(), "ESTUDIANTES");
        panelContenidoProfesor.add(crearListaMateriasProfesor(), "MATERIAS");
        panelContenidoProfesor.add(crearHistorialProfesor(), "HISTORIAL");

        panel.add(panelContenidoProfesor, BorderLayout.CENTER);
        panel.add(crearPiePagina(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelEstudiante() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.FONDO);

        lblNombreEstudiante = new JLabel("ESTUDIANTE");
        lblUsuarioBarraEstudiante = new JLabel("ESTUDIANTE", SwingConstants.RIGHT);
        panel.add(crearBarraSuperiorUsuario("", lblUsuarioBarraEstudiante), BorderLayout.NORTH);
        panel.add(crearMenuLateralEstudiante(), BorderLayout.WEST);

        layoutEstudianteContenido = new CardLayout();
        panelContenidoEstudiante = new JPanel(layoutEstudianteContenido);
        panelContenidoEstudiante.setBackground(UIUtils.FONDO);
        panelContenidoEstudiante.add(crearInicioEstudiante(), "INICIO");
        panelContenidoEstudiante.add(crearConsultaEstudiante(), "CONSULTA");
        panelContenidoEstudiante.add(crearMateriasEstudiante(), "MATERIAS");

        panel.add(panelContenidoEstudiante, BorderLayout.CENTER);
        panel.add(crearPiePagina(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearBarraSuperior(String modulo, String nombreUsuario) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(UIUtils.AZUL_EPN);
        barra.setPreferredSize(new Dimension(100, 76));
        barra.setBorder(new EmptyBorder(8, 18, 8, 18));

        JLabel logo = new JLabel("SAGA");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 26));
        barra.add(logo, BorderLayout.WEST);

        return barra;
    }

    private JPanel crearBarraSuperiorUsuario(String modulo, JLabel usuarioLabel) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(UIUtils.AZUL_EPN);
        barra.setPreferredSize(new Dimension(100, 76));
        barra.setBorder(new EmptyBorder(8, 18, 8, 18));

        JLabel logo = new JLabel("SAGA");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 26));
        barra.add(logo, BorderLayout.WEST);

        JPanel derecha = new JPanel(new GridLayout(2, 1));
        derecha.setOpaque(false);

        JLabel moduloLabel = new JLabel(modulo + "", SwingConstants.RIGHT);
        moduloLabel.setForeground(new Color(200, 200, 200));
        moduloLabel.setFont(new Font("Arial", Font.BOLD, 12));

        usuarioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usuarioLabel.setForeground(Color.WHITE); // El nombre resaltado en blanco
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 15));

        derecha.add(moduloLabel);
        derecha.add(usuarioLabel);

        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    private JPanel crearPiePagina() {
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(UIUtils.AZUL_EPN);
        pie.setPreferredSize(new Dimension(100, 30)); // Altura más pequeña para que se vea más fino
        return pie; // Retornamos el panel vacío solo con el color azul
    }

    private JPanel crearMenuLateralProfesor() {
        JPanel menu = crearMenuBase();
        menu.add(crearEtiquetaBusqueda());
        menu.add(crearBotonMenu("INICIO", () -> layoutProfesorContenido.show(panelContenidoProfesor, "INICIO")));
        menu.add(crearBotonMenu("REGISTRO DE CALIFICACIONES", () -> layoutProfesorContenido.show(panelContenidoProfesor, "REGISTRO")));
        menu.add(crearBotonMenu("ESTUDIANTES INSCRITOS", () -> layoutProfesorContenido.show(panelContenidoProfesor, "ESTUDIANTES")));
        menu.add(crearBotonMenu("MATERIAS ASIGNADAS", () -> layoutProfesorContenido.show(panelContenidoProfesor, "MATERIAS")));
        menu.add(crearBotonMenu("HISTORIAL DE CAMBIOS", () -> layoutProfesorContenido.show(panelContenidoProfesor, "HISTORIAL")));
        menu.add(Box.createVerticalGlue());
        menu.add(crearBotonMenu("CERRAR SESIÓN", this::cerrarSesion));
        return menu;
    }

    private JPanel crearMenuLateralEstudiante() {
        JPanel menu = crearMenuBase();
        menu.add(crearEtiquetaBusqueda());
        menu.add(crearBotonMenu("INICIO", () -> layoutEstudianteContenido.show(panelContenidoEstudiante, "INICIO")));
        menu.add(crearBotonMenu("CONSULTAR CALIFICACIONES", () -> layoutEstudianteContenido.show(panelContenidoEstudiante, "CONSULTA")));
        menu.add(crearBotonMenu("MIS MATERIAS", () -> layoutEstudianteContenido.show(panelContenidoEstudiante, "MATERIAS")));
        menu.add(Box.createVerticalGlue());
        menu.add(crearBotonMenu("CERRAR SESIÓN", this::cerrarSesion));
        return menu;
    }

    private JPanel crearMenuBase() {
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(245, 100));
        menu.setBackground(new Color(239, 242, 247));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(10, 10, 10, 10));
        return menu;
    }

    private JLabel crearEtiquetaBusqueda() {
        JLabel buscar = new JLabel("Buscar menú...");
        buscar.setFont(new Font("Arial", Font.PLAIN, 15));
        buscar.setForeground(new Color(96, 103, 122));
        buscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.BORDE),
                new EmptyBorder(10, 12, 10, 12)));
        buscar.setOpaque(true);
        buscar.setBackground(Color.WHITE);
        buscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return buscar;
    }

    private JButton crearBotonMenu(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(239, 242, 247));
        boton.setForeground(UIUtils.AZUL_EPN);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.DORADO),
                new EmptyBorder(12, 8, 12, 8)));
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    private JPanel crearInicioProfesor() {
        JPanel panel = UIUtils.crearPanelContenido("Panel del Profesor", "Resumen de materias, estudiantes y rendimiento académico.");
        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 12, 12));
        tarjetas.setOpaque(false);

        lblResumenMateriasProfesor = new JLabel("0");
        lblResumenEstudiantesProfesor = new JLabel("0");
        lblResumenPromedioProfesor = new JLabel("0.00");
        lblResumenAprobadosProfesor = new JLabel("0");

        tarjetas.add(UIUtils.crearTarjetaIndicador("Materias asignadas", lblResumenMateriasProfesor));
        tarjetas.add(UIUtils.crearTarjetaIndicador("Estudiantes", lblResumenEstudiantesProfesor));
        tarjetas.add(UIUtils.crearTarjetaIndicador("Promedio general", lblResumenPromedioProfesor));
        tarjetas.add(UIUtils.crearTarjetaIndicador("Registros aprobados", lblResumenAprobadosProfesor));

        // Agrega solo las tarjetas arriba y deja el centro vacío para que se vea solo el fondo
        panel.add(tarjetas, BorderLayout.NORTH);

        //Panel vacío en el centro como relleno estético
        JPanel centroVacio = new JPanel();
        centroVacio.setOpaque(false);
        panel.add(centroVacio, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearRegistroCalificacionesProfesor() {
        JPanel panel = UIUtils.crearPanelContenido(
                "Registro de Calificaciones",
                "Vista similar a un portal académico: carrera, período, materia y tabla de notas."
        );

        JPanel filtros = UIUtils.crearTarjeta();
        filtros.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;

        comboCarreraProfesor = new JComboBox<>();
        comboPeriodoProfesor = new JComboBox<>();
        comboMateriaProfesor = new JComboBox<>();
        comboEstudianteProfesor = new JComboBox<>();

        txtNota1 = new JTextField();
        txtNota2 = new JTextField();
        txtNota3 = new JTextField();
        txtObservacion = new JTextField();
        txtMotivoCambio = new JTextField();

        UIUtils.estilizarCombo(comboCarreraProfesor);
        UIUtils.estilizarCombo(comboPeriodoProfesor);
        UIUtils.estilizarCombo(comboMateriaProfesor);
        UIUtils.estilizarCombo(comboEstudianteProfesor);

        UIUtils.estilizarCampo(txtNota1);
        UIUtils.estilizarCampo(txtNota2);
        UIUtils.estilizarCampo(txtNota3);
        UIUtils.estilizarCampo(txtObservacion);
        UIUtils.estilizarCampo(txtMotivoCambio);

        // Combos más compactos y un poco más altos.
        configurarComboCompacto(comboCarreraProfesor, 260);
        configurarComboCompacto(comboPeriodoProfesor, 120);
        configurarComboCompacto(comboMateriaProfesor, 260);
        configurarComboCompacto(comboEstudianteProfesor, 270);

        // Campos de notas más cortos, altos y centrados para mostrar completo el número.
        configurarCampoCompacto(txtNota1, 115, true);
        configurarCampoCompacto(txtNota2, 115, true);
        configurarCampoCompacto(txtNota3, 115, true);
        configurarCampoCompacto(txtObservacion, 340, false);
        configurarCampoCompacto(txtMotivoCambio, 180, false);

        JButton btnRegistrar = UIUtils.crearBotonPrincipal("Registrar");
        JButton btnModificar = UIUtils.crearBotonSecundario("Modificar");
        JButton btnLimpiar = UIUtils.crearBotonSecundario("Limpiar");

        agregarCampoCompacto(filtros, gbc, 0, 0, "Carrera:", comboCarreraProfesor, 3);
        agregarCampoCompacto(filtros, gbc, 4, 0, "Período:", comboPeriodoProfesor, 1);

        agregarCampoCompacto(filtros, gbc, 0, 1, "Materia:", comboMateriaProfesor, 3);
        agregarCampoCompacto(filtros, gbc, 4, 1, "Estudiante:", comboEstudianteProfesor, 2);

        agregarCampoCompacto(filtros, gbc, 0, 2, "Nota1BIM:", txtNota1, 1);
        agregarCampoCompacto(filtros, gbc, 2, 2, "Nota2BIM:", txtNota2, 1);
        agregarCampoCompacto(filtros, gbc, 4, 2, "Nota3:", txtNota3, 1);

        agregarCampoCompacto(filtros, gbc, 0, 3, "Observaciones:", txtObservacion, 3);
        agregarCampoCompacto(filtros, gbc, 0, 4, "Motivo cambio:", txtMotivoCambio, 1);

        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnRegistrar);
        acciones.add(btnModificar);
        acciones.add(btnLimpiar);
        filtros.add(acciones, gbc);

        // Columna flexible: absorbe el espacio sobrante y evita que los campos se estiren.
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 5;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        filtros.add(Box.createHorizontalGlue(), gbc);
        gbc.gridheight = 1;

        modeloTablaProfesor = new DefaultTableModel(
                new Object[]{
                        "Código", "Paral", "Materia", "N.Mat", "Nota1BIM",
                        "Nota2BIM", "Nota3", "CALIF", "Aprueb", "codcar", "Obs"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = UIUtils.crearTabla(modeloTablaProfesor);
        tabla.getSelectionModel().addListSelectionListener(
                e -> cargarFilaSeleccionadaProfesor(tabla)
        );

        comboCarreraProfesor.addActionListener(e -> cargarPeriodosProfesor());
        comboPeriodoProfesor.addActionListener(e -> cargarMateriasProfesorPorFiltro());

        comboMateriaProfesor.addActionListener(e -> {
            cargarEstudiantesMateriaProfesor();
            refrescarTablaProfesor();
            actualizarResumenProfesor();
        });

        comboEstudianteProfesor.addActionListener(
                e -> cargarRegistroActualEnFormulario()
        );

        btnRegistrar.addActionListener(e -> registrarNotasProfesor());
        btnModificar.addActionListener(e -> modificarNotasProfesor());
        btnLimpiar.addActionListener(e -> limpiarFormularioNotas());

        panel.add(filtros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }


     //Define un tamaño fijo para los JComboBox del formulario.
     //Se establece después de aplicar el estilo para que UIUtils no cambie el tamaño.

    private void configurarComboCompacto(JComboBox<?> combo, int ancho) {
        Dimension dimension = new Dimension(ancho, 38);
        combo.setPreferredSize(dimension);
        combo.setMinimumSize(dimension);
        combo.setMaximumSize(dimension);
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    //Define un tamaño fijo para los campos de texto.
    //Los campos de notas se muestran centrados y con mayor altura.

    private void configurarCampoCompacto(
            JTextField campo,
            int ancho,
            boolean centrarTexto
    ) {
        Dimension dimension = new Dimension(ancho, 40);
        campo.setPreferredSize(dimension);
        campo.setMinimumSize(dimension);
        campo.setMaximumSize(dimension);
        campo.setFont(new Font("Arial", Font.PLAIN, 15));
        campo.setMargin(new Insets(5, 8, 5, 8));

        if (centrarTexto) {
            campo.setHorizontalAlignment(JTextField.CENTER);
        }
    }

    private void agregarCampoCompacto(
            JPanel panel,
            GridBagConstraints gbc,
            int x,
            int y,
            String etiqueta,
            JComponent componente,
            int ancho
    ) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setHorizontalAlignment(SwingConstants.LEFT);

        Dimension tamañoEtiqueta = new Dimension(112, 38);
        label.setPreferredSize(tamañoEtiqueta);
        label.setMinimumSize(tamañoEtiqueta);

        panel.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.gridwidth = ancho;
        panel.add(componente, gbc);
    }

    private JPanel crearListaEstudiantesProfesor() {
        JPanel panel = UIUtils.crearPanelContenido("Estudiantes Inscritos", "Listado de estudiantes según la materia seleccionada.");
        modeloTablaEstudiantesProfesor = new DefaultTableModel(
                new Object[]{"ID", "Estudiante", "Correo", "Carrera", "Nivel"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        panel.add(new JScrollPane(UIUtils.crearTabla(modeloTablaEstudiantesProfesor)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearListaMateriasProfesor() {
        JPanel panel = UIUtils.crearPanelContenido("Materias Asignadas", "Materias que el profesor puede gestionar.");
        modeloTablaMateriasProfesor = new DefaultTableModel(
                new Object[]{"Código", "Materia", "Paralelo", "Período", "Carrera", "Estudiantes", "Promedio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        panel.add(new JScrollPane(UIUtils.crearTabla(modeloTablaMateriasProfesor)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearHistorialProfesor() {
        JPanel panel = UIUtils.crearPanelContenido("Historial de Cambios", "Auditoría básica de modificaciones realizadas por el profesor.");
        modeloTablaHistorialProfesor = new DefaultTableModel(
                new Object[]{"Fecha", "Materia", "Estudiante", "Prom. anterior", "Prom. nuevo", "Profesor", "Motivo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        panel.add(new JScrollPane(UIUtils.crearTabla(modeloTablaHistorialProfesor)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearInicioEstudiante() {
        JPanel panel = UIUtils.crearPanelContenido("Panel del Estudiante", "Consulta personal de calificaciones.");
        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 12, 12));
        tarjetas.setOpaque(false);

        lblCarreraEstudiante = new JLabel("-");
        lblMateriasEstudiante = new JLabel("0");
        lblPromedioEstudiante = new JLabel("0.00");
        lblEstadoEstudiante = new JLabel("- ");

        tarjetas.add(UIUtils.crearTarjetaIndicador("Carrera", lblCarreraEstudiante));
        tarjetas.add(UIUtils.crearTarjetaIndicador("Materias", lblMateriasEstudiante));
        tarjetas.add(UIUtils.crearTarjetaIndicador("Promedio general", lblPromedioEstudiante));
        tarjetas.add(UIUtils.crearTarjetaIndicador("Estado general", lblEstadoEstudiante));

        // Agrega solo las tarjetas arriba y quita el cuadro explicativo inferior
        panel.add(tarjetas, BorderLayout.NORTH);

        JPanel centroVacio = new JPanel();
        centroVacio.setOpaque(false);
        panel.add(centroVacio, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearConsultaEstudiante() {
        JPanel panel = UIUtils.crearPanelContenido("Consulta de Calificaciones", "El estudiante visualiza sus notas por materia.");

        JPanel superior = UIUtils.crearTarjeta();
        superior.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        comboMateriaEstudiante = new JComboBox<>();
        UIUtils.estilizarCombo(comboMateriaEstudiante);
        comboMateriaEstudiante.setPreferredSize(new Dimension(390, 34));
        JButton btnConsultar = UIUtils.crearBotonPrincipal("Consultar");
        JButton btnDescargarPDF = UIUtils.crearBotonSecundario("Descargar PDF");
        superior.add(new JLabel("Materia:"));
        superior.add(comboMateriaEstudiante);
        superior.add(btnConsultar);
        superior.add(btnDescargarPDF);

        areaResumenEstudiante = UIUtils.crearAreaTexto();
        areaResumenEstudiante.setRows(6);

        modeloTablaEstudiante = new DefaultTableModel(
                new Object[]{"Código", "Paral", "Materia", "Nota1BIM", "Nota2BIM", "Nota3", "CALIF", "Aprueb", "Obs"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = UIUtils.crearTabla(modeloTablaEstudiante);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(areaResumenEstudiante), new JScrollPane(tabla));
        split.setResizeWeight(0.34);

        comboMateriaEstudiante.addActionListener(e -> consultarNotasEstudiante());
        btnConsultar.addActionListener(e -> consultarNotasEstudiante());
        btnDescargarPDF.addActionListener(e -> descargarPDFEstudiante());

        panel.add(superior, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearMateriasEstudiante() {
        JPanel panel = UIUtils.crearPanelContenido("Mis Materias", "Materias inscritas en la carrera del estudiante.");
        modeloTablaMateriasEstudiante = new DefaultTableModel(
                new Object[]{"Código", "Materia", "Paralelo", "Período", "Profesor", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        panel.add(new JScrollPane(UIUtils.crearTabla(modeloTablaMateriasEstudiante)), BorderLayout.CENTER);
        return panel;
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int x,
            int y,
            String etiqueta,
            JComponent componente,
            int ancho
    ) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 12));

        Dimension tamañoEtiqueta = new Dimension(120, 38);
        label.setPreferredSize(tamañoEtiqueta);
        label.setMinimumSize(tamañoEtiqueta);

        panel.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.gridwidth = ancho;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;

        panel.add(componente, gbc);
    }

    private void iniciarSesion() {
        String rol = (String) comboRolLogin.getSelectedItem();
        String correo = txtCorreoLogin.getText().trim();
        String password = new String(txtPasswordLogin.getPassword());

        Usuario usuario = buscarUsuarioPorRolYCredenciales(rol, correo, password);
        if (usuario == null) {
            mostrarError("Credenciales incorrectas o el perfil seleccionado no corresponde al usuario.");
            return;
        }

        usuarioActual = usuario;
        if (usuarioActual instanceof Profesor) {
            prepararVistaProfesor((Profesor) usuarioActual);
            layoutPrincipal.show(contenedorPrincipal, "PROFESOR");
        } else if (usuarioActual instanceof Estudiante) {
            prepararVistaEstudiante((Estudiante) usuarioActual);
            layoutPrincipal.show(contenedorPrincipal, "ESTUDIANTE");
        } else if (usuarioActual instanceof Administrador) {
            prepararVistaAdmin((Administrador) usuarioActual);
            layoutPrincipal.show(contenedorPrincipal, "ADMINISTRADOR");
        }
    }

    private Usuario buscarUsuarioPorRolYCredenciales(String rol, String correo, String password) {
        for (Usuario usuario : usuarios) {
            boolean rolCorrecto = ("PROFESOR".equals(rol) && usuario instanceof Profesor)
                    || ("ESTUDIANTE".equals(rol) && usuario instanceof Estudiante)
                    || ("ADMINISTRADOR".equals(rol) && usuario instanceof Administrador);
            if (rolCorrecto && usuario.login(correo, password)) {
                return usuario;
            }
        }
        return null;
    }

    private void prepararVistaProfesor(Profesor profesor) {
        lblNombreProfesor.setText(profesor.getNombre());
        if (lblUsuarioBarraProfesor != null) lblUsuarioBarraProfesor.setText(profesor.getNombre().toUpperCase());
        cargarCarrerasProfesor(profesor);
        refrescarTablasProfesor();
        actualizarResumenProfesor();
        layoutProfesorContenido.show(panelContenidoProfesor, "REGISTRO");
    }

    private void cargarCarrerasProfesor(Profesor profesor) {
        comboCarreraProfesor.removeAllItems();
        Set<Carrera> carrerasAsignadas = new LinkedHashSet<>();
        for (Materia materia : profesor.getMateriasAsignadas()) {
            carrerasAsignadas.add(materia.getCarrera());
        }
        for (Carrera carrera : carrerasAsignadas) {
            comboCarreraProfesor.addItem(carrera);
        }
        cargarPeriodosProfesor();
    }

    private void cargarPeriodosProfesor() {
        if (comboPeriodoProfesor == null) return;
        comboPeriodoProfesor.removeAllItems();
        Carrera carrera = (Carrera) comboCarreraProfesor.getSelectedItem();
        Profesor profesor = (Profesor) usuarioActual;
        Set<String> periodos = new LinkedHashSet<>();
        if (carrera != null && profesor != null) {
            for (Materia materia : profesor.getMateriasAsignadas()) {
                if (materia.getCarrera().equals(carrera)) {
                    periodos.add(materia.getPeriodo());
                }
            }
        }
        for (String periodo : periodos) {
            comboPeriodoProfesor.addItem(periodo);
        }
        cargarMateriasProfesorPorFiltro();
    }

    private void cargarMateriasProfesorPorFiltro() {
        if (comboMateriaProfesor == null) return;
        comboMateriaProfesor.removeAllItems();
        if (!(usuarioActual instanceof Profesor)) return;

        Profesor profesor = (Profesor) usuarioActual;
        Carrera carrera = (Carrera) comboCarreraProfesor.getSelectedItem();
        String periodo = (String) comboPeriodoProfesor.getSelectedItem();

        for (Materia materia : profesor.getMateriasAsignadas()) {
            boolean coincideCarrera = carrera == null || materia.getCarrera().equals(carrera);
            boolean coincidePeriodo = periodo == null || materia.getPeriodo().equals(periodo);
            if (coincideCarrera && coincidePeriodo) {
                comboMateriaProfesor.addItem(materia);
            }
        }
        cargarEstudiantesMateriaProfesor();
        refrescarTablaProfesor();
        actualizarResumenProfesor();
    }

    private void cargarEstudiantesMateriaProfesor() {
        if (comboEstudianteProfesor == null) return;
        comboEstudianteProfesor.removeAllItems();
        Materia materia = (Materia) comboMateriaProfesor.getSelectedItem();
        if (materia == null) return;
        for (Estudiante estudiante : materia.getEstudiantes()) {
            comboEstudianteProfesor.addItem(estudiante);
        }
        cargarRegistroActualEnFormulario();
    }

    private void cargarRegistroActualEnFormulario() {
        Materia materia = comboMateriaProfesor == null ? null : (Materia) comboMateriaProfesor.getSelectedItem();
        Estudiante estudiante = comboEstudianteProfesor == null ? null : (Estudiante) comboEstudianteProfesor.getSelectedItem();
        if (materia == null || estudiante == null || txtNota1 == null) return;

        RegistroNota registro = materia.obtenerRegistroPorEstudiante(estudiante);
        if (registro == null) {
            limpiarFormularioNotas();
        } else {
            txtNota1.setText(formato(registro.getNota1()));
            txtNota2.setText(formato(registro.getNota2()));
            txtNota3.setText(formato(registro.getNota3()));
            txtObservacion.setText(registro.getObservacion());
        }
    }

    private void cargarFilaSeleccionadaProfesor(JTable tabla) {
        if (tabla.getSelectedRow() < 0 || comboMateriaProfesor == null) return;
        Materia materia = (Materia) comboMateriaProfesor.getSelectedItem();
        if (materia == null) return;
        String nombreEstudiante = String.valueOf(tabla.getValueAt(tabla.getSelectedRow(), 3));
        for (int i = 0; i < comboEstudianteProfesor.getItemCount(); i++) {
            Estudiante estudiante = comboEstudianteProfesor.getItemAt(i);
            if (estudiante.getNombre().equals(nombreEstudiante)) {
                comboEstudianteProfesor.setSelectedItem(estudiante);
                break;
            }
        }
    }

    private void registrarNotasProfesor() {
        try {
            Profesor profesor = (Profesor) usuarioActual;

            Materia materia =
                    (Materia) comboMateriaProfesor.getSelectedItem();

            Estudiante estudiante =
                    (Estudiante) comboEstudianteProfesor.getSelectedItem();

            validarSeleccion(materia, estudiante);

            /*
             * Verificar antes de registrar.
             */
            if (materia.obtenerRegistroPorEstudiante(estudiante) != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "El estudiante ya tiene calificaciones en esta materia. "
                                + "Use la opción Modificar."
                );
                return;
            }

            float n1 =
                    calificacionService.convertirNota(txtNota1.getText());

            float n2 =
                    calificacionService.convertirNota(txtNota2.getText());

            float n3 =
                    calificacionService.convertirNota(txtNota3.getText());

            boolean registrado = calificacionService.registrarNotas(
                    profesor,
                    materia,
                    estudiante,
                    n1,
                    n2,
                    n3,
                    txtObservacion.getText().trim()
            );

            if (!registrado) {
                mostrarError(
                        "No se pudieron registrar las calificaciones."
                );
                return;
            }

            guardarDatosAutomatico();
            refrescarTablasProfesor();
            actualizarResumenProfesor();

            JOptionPane.showMessageDialog(
                    this,
                    "Calificaciones registradas y guardadas correctamente."
            );

        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void modificarNotasProfesor() {
        try {
            Profesor profesor = (Profesor) usuarioActual;
            Materia materia = (Materia) comboMateriaProfesor.getSelectedItem();
            Estudiante estudiante = (Estudiante) comboEstudianteProfesor.getSelectedItem();
            validarSeleccion(materia, estudiante);

            float n1 = calificacionService.convertirNota(txtNota1.getText());
            float n2 = calificacionService.convertirNota(txtNota2.getText());
            float n3 = calificacionService.convertirNota(txtNota3.getText());
            boolean ok = calificacionService.modificarNotas(profesor, materia, estudiante, n1, n2, n3,
                    txtObservacion.getText().trim(), txtMotivoCambio.getText().trim());
            guardarDatosAutomatico();
            refrescarTablasProfesor();
            refrescarTablaHistorialProfesor();
            actualizarResumenProfesor();
            JOptionPane.showMessageDialog(this, ok
                    ? "Calificaciones modificadas y auditadas correctamente."
                    : "No existe un registro previo. Use la opción Registrar.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void validarSeleccion(Materia materia, Estudiante estudiante) {
        if (materia == null) throw new IllegalArgumentException("Seleccione una materia.");
        if (estudiante == null) throw new IllegalArgumentException("Seleccione un estudiante.");
    }

    private void limpiarFormularioNotas() {
        if (txtNota1 == null) return;
        txtNota1.setText("");
        txtNota2.setText("");
        txtNota3.setText("");
        txtObservacion.setText("");
        if (txtMotivoCambio != null) txtMotivoCambio.setText("");
    }

    private void refrescarTablasProfesor() {
        refrescarTablaProfesor();
        refrescarTablaEstudiantesProfesor();
        refrescarTablaMateriasProfesor();
        refrescarTablaHistorialProfesor();
    }

    private void refrescarTablaProfesor() {
        if (modeloTablaProfesor == null) return;
        modeloTablaProfesor.setRowCount(0);
        Materia materia = (Materia) comboMateriaProfesor.getSelectedItem();
        if (materia == null) return;

        for (Estudiante estudiante : materia.getEstudiantes()) {
            RegistroNota registro = materia.obtenerRegistroPorEstudiante(estudiante);
            if (registro == null) {
                modeloTablaProfesor.addRow(new Object[]{
                        materia.getCodigo(), materia.getParalelo(), materia.getNombreMateria(), estudiante.getNombre(),
                        "", "", "", "", "Pendiente", materia.getCarrera().getCodigo(), "Sin registro"
                });
            } else {
                modeloTablaProfesor.addRow(new Object[]{
                        materia.getCodigo(), materia.getParalelo(), materia.getNombreMateria(), estudiante.getNombre(),
                        formato(registro.getNota1()), formato(registro.getNota2()), formato(registro.getNota3()),
                        formato(registro.calcularPromedio()), registro.calcularCodigoAprobacion(),
                        materia.getCarrera().getCodigo(), registro.getObservacion()
                });
            }
        }
    }

    private void refrescarTablaEstudiantesProfesor() {
        if (modeloTablaEstudiantesProfesor == null) return;
        modeloTablaEstudiantesProfesor.setRowCount(0);
        Materia materia = comboMateriaProfesor == null ? null : (Materia) comboMateriaProfesor.getSelectedItem();
        if (materia == null) return;
        for (Estudiante estudiante : materia.getEstudiantes()) {
            modeloTablaEstudiantesProfesor.addRow(new Object[]{
                    estudiante.getId(), estudiante.getNombre(), estudiante.getCorreo(),
                    estudiante.getCarrera().getNombre(), estudiante.getNivel()
            });
        }
    }

    private void refrescarTablaMateriasProfesor() {
        if (modeloTablaMateriasProfesor == null || !(usuarioActual instanceof Profesor)) return;
        modeloTablaMateriasProfesor.setRowCount(0);
        Profesor profesor = (Profesor) usuarioActual;
        for (Materia materia : profesor.getMateriasAsignadas()) {
            modeloTablaMateriasProfesor.addRow(new Object[]{
                    materia.getCodigo(), materia.getNombreMateria(), materia.getParalelo(), materia.getPeriodo(),
                    materia.getCarrera().getNombre(), materia.getEstudiantes().size(), formato(materia.calcularPromedioMateria())
            });
        }
    }

    private void actualizarResumenProfesor() {
        if (!(usuarioActual instanceof Profesor) || lblResumenMateriasProfesor == null) return;
        Profesor profesor = (Profesor) usuarioActual;
        int materiasAsignadas = profesor.getMateriasAsignadas().size();
        Set<Estudiante> estudiantesUnicos = new LinkedHashSet<>();
        int registros = 0;
        int aprobados = 0;
        float suma = 0;

        for (Materia materia : profesor.getMateriasAsignadas()) {
            estudiantesUnicos.addAll(materia.getEstudiantes());
            for (RegistroNota registro : materia.getRegistrosNotas()) {
                registros++;
                suma += registro.calcularPromedio();
                if (registro.calcularPromedio() >= 14) aprobados++;
            }
        }
        lblResumenMateriasProfesor.setText(String.valueOf(materiasAsignadas));
        lblResumenEstudiantesProfesor.setText(String.valueOf(estudiantesUnicos.size()));
        lblResumenPromedioProfesor.setText(registros == 0 ? "0.00" : formato(suma / registros));
        lblResumenAprobadosProfesor.setText(String.valueOf(aprobados));
    }

    private void prepararVistaEstudiante(Estudiante estudiante) {
        lblNombreEstudiante.setText(estudiante.getNombre());
        if (lblUsuarioBarraEstudiante != null) lblUsuarioBarraEstudiante.setText(estudiante.getNombre().toUpperCase());
        comboMateriaEstudiante.removeAllItems();
        for (Materia materia : estudiante.getCarrera().getMaterias()) {
            if (materia.estaInscrito(estudiante)) {
                comboMateriaEstudiante.addItem(materia);
            }
        }
        refrescarResumenEstudiante();
        refrescarTablaMateriasEstudiante();
        consultarNotasEstudiante();
        layoutEstudianteContenido.show(panelContenidoEstudiante, "CONSULTA");
    }

    private void consultarNotasEstudiante() {
        if (!(usuarioActual instanceof Estudiante) || modeloTablaEstudiante == null) return;
        Estudiante estudiante = (Estudiante) usuarioActual;
        Materia materia = (Materia) comboMateriaEstudiante.getSelectedItem();
        modeloTablaEstudiante.setRowCount(0);
        areaResumenEstudiante.setText(estudiante.verNota(materia));
        if (materia == null) return;

        RegistroNota registro = materia.obtenerRegistroPorEstudiante(estudiante);
        if (registro == null) {
            modeloTablaEstudiante.addRow(new Object[]{
                    materia.getCodigo(), materia.getParalelo(), materia.getNombreMateria(), "", "", "", "", "Pendiente", "Sin registro"
            });
        } else {
            modeloTablaEstudiante.addRow(new Object[]{
                    materia.getCodigo(), materia.getParalelo(), materia.getNombreMateria(),
                    formato(registro.getNota1()), formato(registro.getNota2()), formato(registro.getNota3()),
                    formato(registro.calcularPromedio()), registro.calcularCodigoAprobacion(), registro.getObservacion()
            });
        }
    }

    private void refrescarResumenEstudiante() {
        if (!(usuarioActual instanceof Estudiante) || lblCarreraEstudiante == null) return;
        Estudiante estudiante = (Estudiante) usuarioActual;
        int materiasInscritas = 0;
        int registros = 0;
        float suma = 0;
        for (Materia materia : estudiante.getCarrera().getMaterias()) {
            if (materia.estaInscrito(estudiante)) {
                materiasInscritas++;
                RegistroNota registro = materia.obtenerRegistroPorEstudiante(estudiante);
                if (registro != null) {
                    registros++;
                    suma += registro.calcularPromedio();
                }
            }
        }
        float promedio = registros == 0 ? 0 : suma / registros;
        lblCarreraEstudiante.setText(estudiante.getCarrera().getCodigo());
        lblMateriasEstudiante.setText(String.valueOf(materiasInscritas));
        lblPromedioEstudiante.setText(formato(promedio));
        lblEstadoEstudiante.setText(registros == 0 ? "Pendiente" : (promedio >= 14 ? "Aprobado" : (promedio >= 9 ? "Recuperación" : "Reprobado")));
    }

    private void refrescarTablaMateriasEstudiante() {
        if (!(usuarioActual instanceof Estudiante) || modeloTablaMateriasEstudiante == null) return;
        Estudiante estudiante = (Estudiante) usuarioActual;
        modeloTablaMateriasEstudiante.setRowCount(0);
        for (Materia materia : estudiante.getCarrera().getMaterias()) {
            if (materia.estaInscrito(estudiante)) {
                RegistroNota registro = materia.obtenerRegistroPorEstudiante(estudiante);
                modeloTablaMateriasEstudiante.addRow(new Object[]{
                        materia.getCodigo(), materia.getNombreMateria(), materia.getParalelo(), materia.getPeriodo(),
                        materia.getProfesor().getNombre(), registro == null ? "Pendiente" : registro.calcularEstado(registro.calcularPromedio())
                });
            }
        }
    }

    private void refrescarPerfiles() {
        // Método mantenido para compatibilidad de dependencias internas
    }

    private void refrescarTablaHistorialProfesor() {
        if (modeloTablaHistorialProfesor == null || !(usuarioActual instanceof Profesor)) return;
        modeloTablaHistorialProfesor.setRowCount(0);
        Profesor profesor = (Profesor) usuarioActual;
        for (Materia materia : profesor.getMateriasAsignadas()) {
            for (HistorialNota historial : materia.getHistorialNotas()) {
                modeloTablaHistorialProfesor.addRow(new Object[]{
                        historial.getFechaCambioTexto(),
                        materia.getCodigo() + " - " + materia.getNombreMateria(),
                        historial.getEstudiante().getNombre(),
                        formato(historial.getNotaAnterior()),
                        formato(historial.getNotaNueva()),
                        historial.getProfesor().getNombre(),
                        historial.getMotivo()
                });
            }
        }
    }

    private void guardarDatosAutomatico() {
        try {
            persistenciaService.guardarNotas(ARCHIVO_PERSISTENCIA, materias);
        } catch (IOException ex) {
            mostrarError("Las notas fueron procesadas, pero falló el guardado automático en la BD: " + ex.getMessage());
        }
    }

    private void descargarPDFEstudiante() {
        try {
            if (!(usuarioActual instanceof Estudiante)) {
                mostrarError("Solo el estudiante puede descargar su reporte personal.");
                return;
            }
            Estudiante estudiante = (Estudiante) usuarioActual;
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar reporte PDF");
            chooser.setSelectedFile(new File("notas_" + estudiante.getId() + ".pdf"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                reporteService.generarPDFNotasEstudiante(chooser.getSelectedFile().toPath(), estudiante);
                JOptionPane.showMessageDialog(this, "PDF generado correctamente.");
            }
        } catch (Exception ex) {
            mostrarError("No se pudo generar el PDF: " + ex.getMessage());
        }
    }

    private void cerrarSesion() {
        usuarioActual = null;
        txtPasswordLogin.setText("");
        layoutPrincipal.show(contenedorPrincipal, "LOGIN");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String formato(float valor) {
        return String.format("%.2f", valor);
    }

    private void cargarDatosIniciales() {
        usuarios.clear();
        estudiantes.clear();
        profesores.clear();
        carreras.clear();
        materias.clear();

        try {
            /*
             * Cargar administradores directamente desde MySQL.
             */
            List<Administrador> administradoresCargados =
                    persistenciaService.cargarAdministradores();

            usuarios.addAll(administradoresCargados);

            if (!administradoresCargados.isEmpty()) {
                administrador = administradoresCargados.get(0);
            } else {
                administrador = null;

                System.out.println(
                        "No existe ningún administrador registrado en MySQL."
                );
            }

            /*
             * Aunque los métodos reciben Path por compatibilidad con
             * PersistenciaService, todos realizan consultas SQL.
             */
            carreras.addAll(
                    persistenciaService.cargarCarreras(null)
            );

            profesores.addAll(
                    persistenciaService.cargarProfesores(null)
            );

            usuarios.addAll(profesores);

            estudiantes.addAll(
                    persistenciaService.cargarEstudiantes(
                            null,
                            carreras
                    )
            );

            usuarios.addAll(estudiantes);

            materias.addAll(
                    persistenciaService.cargarMaterias(
                            null,
                            carreras,
                            profesores
                    )
            );

            persistenciaService.cargarNotasSiExiste(
                    null,
                    materias,
                    estudiantes,
                    profesores
            );

            System.out.println("Datos cargados correctamente desde MySQL.");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudieron cargar los datos desde MySQL.\n\n"
                            + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void registrarEstudianteEnSistema(Estudiante estudiante) {
        administrador.registrarEstudiante(estudiantes, estudiante);
        usuarios.add(estudiante);
    }

    private void inscribirEnMateriasDeCarrera(Estudiante estudiante) {
        for (Materia materia : estudiante.getCarrera().getMaterias()) {
            administrador.asignarEstudianteAMateria(materia, estudiante);
        }
    }

    // =========================================================
    // VARIABLES Y MÉTODOS DEL PANEL ADMINISTRADOR
    // =========================================================
    private CardLayout layoutAdminContenido;
    private JPanel panelContenidoAdmin;
    private JLabel lblUsuarioBarraAdmin;
    private JTextField txtIdEstudianteForm;
    private JTextField txtNombreEstudianteForm;
    private JTextField txtCorreoEstudianteForm;
    private JPasswordField txtPasswordEstudianteForm;
    private JComboBox<Carrera> comboCarreraForm;
    private JSpinner spinNivelForm;

    private JTextField txtIdProfesorForm;
    private JTextField txtNombreProfesorForm;
    private JTextField txtCorreoProfesorForm;
    private JPasswordField txtPasswordProfesorForm;
    private JTextField txtDepartamentoForm;

    private JTextField txtCodigoMateriaForm;
    private JTextField txtNombreMateriaForm;
    private JTextField txtParaleloMateriaForm;
    private JTextField txtPeriodoMateriaForm;
    private JComboBox<Carrera> comboCarreraMateriaForm;
    private JComboBox<Profesor> comboProfesorMateriaForm;

    private JComboBox<Materia> comboMateriaInscripcionForm;
    private JComboBox<Estudiante> comboEstudianteInscripcionForm;

    private void prepararVistaAdmin(Administrador admin) {
        /*
         * Guardar como administrador activo al usuario que realmente
         * inició sesión desde MySQL.
         */
        administrador = admin;

        if (lblUsuarioBarraAdmin != null) {
            lblUsuarioBarraAdmin.setText(
                    admin.getNombre().toUpperCase()
            );
        }

        layoutAdminContenido.show(
                panelContenidoAdmin,
                "REG_ESTUDIANTE"
        );
    }

    private JPanel crearPanelAdministrador() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.FONDO);

        lblUsuarioBarraAdmin = new JLabel("ADMINISTRADOR", SwingConstants.RIGHT);
        panel.add(crearBarraSuperiorUsuario("MÓDULO: ADMINISTRACIÓN", lblUsuarioBarraAdmin), BorderLayout.NORTH);

        JPanel menu = crearMenuBase();
        menu.add(crearEtiquetaBusqueda());
        menu.add(crearBotonMenu("NUEVA CARRERA", () -> layoutAdminContenido.show(panelContenidoAdmin, "REG_CARRERA")));
        menu.add(crearBotonMenu("NUEVO ESTUDIANTE", () -> {
            actualizarCombosEstudiante();
            layoutAdminContenido.show(panelContenidoAdmin, "REG_ESTUDIANTE");
        }));
        menu.add(crearBotonMenu("NUEVO PROFESOR", () -> layoutAdminContenido.show(panelContenidoAdmin, "REG_PROFESOR")));
        menu.add(crearBotonMenu("NUEVA MATERIA", () -> {
            actualizarCombosMateria();
            layoutAdminContenido.show(panelContenidoAdmin, "REG_MATERIA");
        }));
        menu.add(crearBotonMenu("INSCRIBIR ALUMNO", () -> {
            actualizarCombosInscripcion();
            layoutAdminContenido.show(panelContenidoAdmin, "REG_INSCRIPCION");
        }));
        menu.add(Box.createVerticalGlue());
        menu.add(crearBotonMenu("CERRAR SESIÓN", this::cerrarSesion));
        panel.add(menu, BorderLayout.WEST);

        layoutAdminContenido = new CardLayout();
        panelContenidoAdmin = new JPanel(layoutAdminContenido);
        panelContenidoAdmin.setBackground(UIUtils.FONDO);

        panelContenidoAdmin.add(crearFormularioRegistroCarrera(), "REG_CARRERA");
        panelContenidoAdmin.add(crearFormularioRegistroEstudiante(), "REG_ESTUDIANTE");
        panelContenidoAdmin.add(crearFormularioRegistroProfesor(), "REG_PROFESOR");
        panelContenidoAdmin.add(crearFormularioRegistroMateria(), "REG_MATERIA");
        panelContenidoAdmin.add(crearFormularioInscripcionEstudiante(), "REG_INSCRIPCION");

        panel.add(panelContenidoAdmin, BorderLayout.CENTER);
        panel.add(crearPiePagina(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormularioRegistroEstudiante() {
        JPanel panel = UIUtils.crearPanelContenido("Registrar Nuevo Estudiante", "Agregue estudiantes al sistema. Los datos se guardarán automáticamente en MySQL.");

        JPanel formulario = UIUtils.crearTarjeta();
        formulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdEstudianteForm = new JTextField();
        txtNombreEstudianteForm = new JTextField();
        txtCorreoEstudianteForm = new JTextField();
        txtPasswordEstudianteForm = new JPasswordField();
        comboCarreraForm = new JComboBox<>();
        for (Carrera c : carreras) {
            comboCarreraForm.addItem(c);
        }
        spinNivelForm = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        UIUtils.estilizarCampo(txtIdEstudianteForm);
        UIUtils.estilizarCampo(txtNombreEstudianteForm);
        UIUtils.estilizarCampo(txtCorreoEstudianteForm);
        UIUtils.estilizarCampo(txtPasswordEstudianteForm);
        UIUtils.estilizarCombo(comboCarreraForm);

        agregarCampo(formulario, gbc, 0, 0, "ID Único / Código único:", txtIdEstudianteForm, 1);
        agregarCampo(formulario, gbc, 0, 1, "Nombre Completo:", txtNombreEstudianteForm, 1);
        agregarCampo(formulario, gbc, 0, 2, "Correo Institucional:", txtCorreoEstudianteForm, 1);
        agregarCampo(formulario, gbc, 0, 3, "Contraseña:", txtPasswordEstudianteForm, 1);
        agregarCampo(formulario, gbc, 0, 4, "Carrera Asignada:", comboCarreraForm, 1);
        agregarCampo(formulario, gbc, 0, 5, "Nivel (Semestre):", spinNivelForm, 1);

        JButton btnGuardar = UIUtils.crearBotonPrincipal("Guardar en Base de Datos");
        gbc.gridx = 1; gbc.gridy = 6;
        formulario.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> guardarNuevoEstudiante());

        panel.add(formulario, BorderLayout.NORTH);
        return panel;
    }

    private void guardarNuevoEstudiante() {
        try {
            String id = txtIdEstudianteForm.getText().trim();
            String nombre = txtNombreEstudianteForm.getText().trim();
            String correo = txtCorreoEstudianteForm.getText().trim();
            String password = new String(txtPasswordEstudianteForm.getPassword()).trim();
            Carrera carrera = (Carrera) comboCarreraForm.getSelectedItem();
            int nivel = (Integer) spinNivelForm.getValue();

            if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || carrera == null) {
                mostrarError("Por favor, llene todos los campos del formulario.");
                return;
            }

            Estudiante nuevo = new Estudiante(id, nombre, correo, password, carrera, nivel);
            administrador.registrarEstudiante(estudiantes, nuevo);
            usuarios.add(nuevo);

            persistenciaService.guardarEstudiantes(Paths.get("data", "estudiantes.csv"), estudiantes);
            JOptionPane.showMessageDialog(this, "Estudiante registrado y guardado exitosamente.");

            txtIdEstudianteForm.setText("");
            txtNombreEstudianteForm.setText("");
            txtCorreoEstudianteForm.setText("");
            txtPasswordEstudianteForm.setText("");

        } catch (Exception ex) {
            mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    private JPanel crearFormularioRegistroProfesor() {
        JPanel panel = UIUtils.crearPanelContenido("Registrar Nuevo Profesor", "Agregue docentes al sistema. Los datos se guardarán automáticamente en MySQL.");

        JPanel formulario = UIUtils.crearTarjeta();
        formulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdProfesorForm = new JTextField();
        txtNombreProfesorForm = new JTextField();
        txtCorreoProfesorForm = new JTextField();
        txtPasswordProfesorForm = new JPasswordField();
        txtDepartamentoForm = new JTextField();

        UIUtils.estilizarCampo(txtIdProfesorForm);
        UIUtils.estilizarCampo(txtNombreProfesorForm);
        UIUtils.estilizarCampo(txtCorreoProfesorForm);
        UIUtils.estilizarCampo(txtPasswordProfesorForm);
        UIUtils.estilizarCampo(txtDepartamentoForm);

        agregarCampo(formulario, gbc, 0, 0, "ID Único / Código único:", txtIdProfesorForm, 1);
        agregarCampo(formulario, gbc, 0, 1, "Nombre Completo:", txtNombreProfesorForm, 1);
        agregarCampo(formulario, gbc, 0, 2, "Correo Institucional:", txtCorreoProfesorForm, 1);
        agregarCampo(formulario, gbc, 0, 3, "Contraseña:", txtPasswordProfesorForm, 1);
        agregarCampo(formulario, gbc, 0, 4, "Departamento Académico:", txtDepartamentoForm, 1);

        JButton btnGuardar = UIUtils.crearBotonPrincipal("Guardar Profesor");
        gbc.gridx = 1; gbc.gridy = 5;
        formulario.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> guardarNuevoProfesor());

        panel.add(formulario, BorderLayout.NORTH);
        return panel;
    }

    private void guardarNuevoProfesor() {
        try {
            String id = txtIdProfesorForm.getText().trim();
            String nombre = txtNombreProfesorForm.getText().trim();
            String correo = txtCorreoProfesorForm.getText().trim();
            String password = new String(txtPasswordProfesorForm.getPassword()).trim();
            String departamento = txtDepartamentoForm.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || departamento.isEmpty()) {
                mostrarError("Por favor, llene todos los campos del formulario.");
                return;
            }

            Profesor nuevo = new Profesor(id, nombre, correo, password, departamento);
            profesores.add(nuevo);
            usuarios.add(nuevo);

            persistenciaService.guardarProfesores(Paths.get("data", "profesores.csv"), profesores);
            JOptionPane.showMessageDialog(this, "Profesor registrado y guardado exitosamente.");

            txtIdProfesorForm.setText("");
            txtNombreProfesorForm.setText("");
            txtCorreoProfesorForm.setText("");
            txtPasswordProfesorForm.setText("");
            txtDepartamentoForm.setText("");

        } catch (Exception ex) {
            mostrarError("Error al guardar: " + ex.getMessage());
        }
    }

    private JPanel crearFormularioRegistroMateria() {
        JPanel panel = UIUtils.crearPanelContenido("Registrar Nueva Materia", "Cree una asignatura y asígnele un docente. Los datos se guardarán en MySQL.");

        JPanel formulario = UIUtils.crearTarjeta();
        formulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigoMateriaForm = new JTextField();
        txtNombreMateriaForm = new JTextField();
        txtParaleloMateriaForm = new JTextField();
        txtPeriodoMateriaForm = new JTextField("2026-A");
        comboCarreraMateriaForm = new JComboBox<>();
        comboProfesorMateriaForm = new JComboBox<>();

        UIUtils.estilizarCampo(txtCodigoMateriaForm);
        UIUtils.estilizarCampo(txtNombreMateriaForm);
        UIUtils.estilizarCampo(txtParaleloMateriaForm);
        UIUtils.estilizarCampo(txtPeriodoMateriaForm);
        UIUtils.estilizarCombo(comboCarreraMateriaForm);
        UIUtils.estilizarCombo(comboProfesorMateriaForm);

        agregarCampo(formulario, gbc, 0, 0, "Código Materia (ej: SIS310):", txtCodigoMateriaForm, 1);
        agregarCampo(formulario, gbc, 0, 1, "Nombre de la Materia:", txtNombreMateriaForm, 1);
        agregarCampo(formulario, gbc, 0, 2, "Paralelo (ej: GR1):", txtParaleloMateriaForm, 1);
        agregarCampo(formulario, gbc, 0, 3, "Período Académico:", txtPeriodoMateriaForm, 1);
        agregarCampo(formulario, gbc, 0, 4, "Carrera Perteneciente:", comboCarreraMateriaForm, 1);
        agregarCampo(formulario, gbc, 0, 5, "Docente Asignado:", comboProfesorMateriaForm, 1);

        JButton btnGuardar = UIUtils.crearBotonPrincipal("Guardar Materia");
        gbc.gridx = 1; gbc.gridy = 6;
        formulario.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> guardarNuevaMateria());

        panel.add(formulario, BorderLayout.NORTH);
        return panel;
    }

    private void actualizarCombosMateria() {
        comboCarreraMateriaForm.removeAllItems();
        for (Carrera c : carreras) comboCarreraMateriaForm.addItem(c);

        comboProfesorMateriaForm.removeAllItems();
        for (Profesor p : profesores) comboProfesorMateriaForm.addItem(p);
    }

    private void guardarNuevaMateria() {
        try {
            String codigo = txtCodigoMateriaForm.getText().trim();
            String nombre = txtNombreMateriaForm.getText().trim();
            String paralelo = txtParaleloMateriaForm.getText().trim();
            String periodo = txtPeriodoMateriaForm.getText().trim();
            Carrera carrera = (Carrera) comboCarreraMateriaForm.getSelectedItem();
            Profesor profesor = (Profesor) comboProfesorMateriaForm.getSelectedItem();

            if (codigo.isEmpty() || nombre.isEmpty() || paralelo.isEmpty() || periodo.isEmpty() || carrera == null || profesor == null) {
                mostrarError("Por favor, llene todos los campos de texto.");
                return;
            }

            Materia nueva = new Materia(codigo, nombre, paralelo, periodo, carrera, profesor);
            materias.add(nueva);
            profesor.asignarMateria(nueva);

            persistenciaService.guardarMaterias(Paths.get("data", "materias.csv"), materias);
            JOptionPane.showMessageDialog(this, "Materia registrada y asignada al profesor correctamente.");

            txtCodigoMateriaForm.setText("");
            txtNombreMateriaForm.setText("");
            txtParaleloMateriaForm.setText("");

        } catch (Exception ex) {
            mostrarError("Error al guardar materia: " + ex.getMessage());
        }
    }

    private JPanel crearFormularioInscripcionEstudiante() {
        JPanel panel = UIUtils.crearPanelContenido("Inscribir Alumno en Materia", "Matricule un estudiante en una materia para que aparezca en el listado del profesor.");

        JPanel formulario = UIUtils.crearTarjeta();
        formulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboMateriaInscripcionForm = new JComboBox<>();
        comboEstudianteInscripcionForm = new JComboBox<>();

        UIUtils.estilizarCombo(comboMateriaInscripcionForm);
        UIUtils.estilizarCombo(comboEstudianteInscripcionForm);
        comboMateriaInscripcionForm.setPreferredSize(new Dimension(300, 34));
        comboEstudianteInscripcionForm.setPreferredSize(new Dimension(300, 34));

        agregarCampo(formulario, gbc, 0, 0, "Seleccione la Materia:", comboMateriaInscripcionForm, 1);
        agregarCampo(formulario, gbc, 0, 1, "Seleccione al Estudiante:", comboEstudianteInscripcionForm, 1);

        JButton btnInscribir = UIUtils.crearBotonPrincipal("Confirmar Inscripción Académica");
        gbc.gridx = 1; gbc.gridy = 2;
        formulario.add(btnInscribir, gbc);

        btnInscribir.addActionListener(e -> inscribirEstudianteEnMateria());

        panel.add(formulario, BorderLayout.NORTH);
        return panel;
    }

    private void actualizarCombosInscripcion() {
        comboMateriaInscripcionForm.removeAllItems();
        for (Materia m : materias) comboMateriaInscripcionForm.addItem(m);

        comboEstudianteInscripcionForm.removeAllItems();
        for (Estudiante est : estudiantes) comboEstudianteInscripcionForm.addItem(est);
    }

    private void inscribirEstudianteEnMateria() {
        try {
            Materia materia =
                    (Materia) comboMateriaInscripcionForm.getSelectedItem();

            Estudiante estudiante =
                    (Estudiante) comboEstudianteInscripcionForm.getSelectedItem();

            if (materia == null || estudiante == null) {
                mostrarError(
                        "Asegúrese de haber seleccionado una materia y un estudiante."
                );
                return;
            }

            /*
             * Solo inscribe al estudiante en la materia.
             * No crea todavía un registro de notas.
             */
            administrador.asignarEstudianteAMateria(
                    materia,
                    estudiante
            );

            /*
             * PersistenciaBDService guardará la relación en
             * materia_estudiante, pero no creará una nota mientras
             * no exista un RegistroNota.
             */
            persistenciaService.guardarNotas(
                    null,
                    materias
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante inscrito correctamente. "
                            + "El profesor ya puede registrar sus primeras notas."
            );

        } catch (Exception ex) {
            mostrarError(
                    "Error al procesar inscripción: "
                            + ex.getMessage()
            );
        }
    }

    private JPanel crearFormularioRegistroCarrera() {
        JPanel panel = UIUtils.crearPanelContenido("Registrar Nueva Carrera", "Defina nuevas carreras en la facultad. Los datos se guardarán en MySQL.");

        JPanel formulario = UIUtils.crearTarjeta();
        formulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigoCarreraForm = new JTextField();
        txtNombreCarreraForm = new JTextField();

        UIUtils.estilizarCampo(txtCodigoCarreraForm);
        UIUtils.estilizarCampo(txtNombreCarreraForm);

        agregarCampo(formulario, gbc, 0, 0, "Código de la Carrera (ej: SIS01):", txtCodigoCarreraForm, 1);
        agregarCampo(formulario, gbc, 0, 1, "Nombre Completo de la Carrera:", txtNombreCarreraForm, 1);

        JButton btnGuardar = UIUtils.crearBotonPrincipal("Guardar Carrera");
        gbc.gridx = 1; gbc.gridy = 2;
        formulario.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> guardarNuevaCarrera());

        panel.add(formulario, BorderLayout.NORTH);
        return panel;
    }

    private void guardarNuevaCarrera() {
        try {
            String codigo = txtCodigoCarreraForm.getText().trim();
            String nombre = txtNombreCarreraForm.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty()) {
                mostrarError("Por favor, llene todos los campos del formulario.");
                return;
            }

            Carrera nueva = new Carrera(codigo, nombre);
            carreras.add(nueva);

            persistenciaService.guardarCarreras(Paths.get("data", "carreras.csv"), carreras);
            JOptionPane.showMessageDialog(this, "Carrera registrada exitosamente en el sistema.");

            txtCodigoCarreraForm.setText("");
            txtNombreCarreraForm.setText("");

        } catch (Exception ex) {
            mostrarError("Error al guardar carrera: " + ex.getMessage());
        }
    }

    private void actualizarCombosEstudiante() {
        if (comboCarreraForm == null) return;
        comboCarreraForm.removeAllItems();
        for (Carrera c : carreras) comboCarreraForm.addItem(c);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TextField.foreground", Color.BLACK);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("PasswordField.foreground", Color.BLACK);
            UIManager.put("PasswordField.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", Color.BLACK);
            UIManager.put("ComboBox.selectionForeground", Color.BLACK);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.selectionBackground", new Color(218, 230, 250));
            UIManager.put("Table.foreground", Color.BLACK);
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.selectionForeground", Color.BLACK);
            UIManager.put("Table.selectionBackground", new Color(218, 230, 250));
            UIManager.put("Viewport.background", Color.WHITE);
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new SistemaCalificacionesGUI().setVisible(true));
    }
}