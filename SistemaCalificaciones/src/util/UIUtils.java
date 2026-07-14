package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIUtils {

    public static final Color AZUL_EPN = new Color(22, 36, 62);
    public static final Color AZUL_CLARO = new Color(34, 73, 130);
    public static final Color FONDO = new Color(245, 247, 251);
    public static final Color BORDE = new Color(213, 221, 235);
    public static final Color DORADO = new Color(218, 166, 48);
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 18);
    public static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 13);

    public static JButton crearBotonPrincipal(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_EPN);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(10, 20, 10, 20));
        return boton;
    }

    public static JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(AZUL_EPN);
        boton.setBackground(new Color(225, 233, 245));
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(10, 20, 10, 20));
        return boton;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 16)); // Aumentamos la fuente para que se lea mejor
        campo.setPreferredSize(new Dimension(300, 45));   // Aumentamos ancho y alto
        campo.setForeground(Color.BLACK);
        campo.setBackground(Color.WHITE);
        campo.setCaretColor(Color.BLACK);
        campo.setDisabledTextColor(Color.DARK_GRAY);
        campo.setOpaque(true);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                new EmptyBorder(5, 10, 5, 10))); // Espacio interno para que no quede apretado
    }

    public static void hacerCampoGrande(JTextField campo, int ancho) {
        campo.setMinimumSize(new Dimension(ancho, 20));
        campo.setMaximumSize(new Dimension(ancho, 20));
        campo.setForeground(Color.BLACK);
        campo.setBackground(Color.WHITE);
        campo.setCaretColor(Color.BLACK);
        campo.setFont(FUENTE_NORMAL);
        campo.setOpaque(true);
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 16));
        combo.setBackground(Color.WHITE);
        combo.setForeground(Color.BLACK);
        combo.setPreferredSize(new Dimension(300, 45));
        combo.setOpaque(true);
        aplicarRendererCombo(combo);
    }

    public static void hacerComboGrande(JComboBox<?> combo, int ancho) {
        combo.setMinimumSize(new Dimension(ancho, 20));
        combo.setMaximumSize(new Dimension(ancho, 20));
        combo.setForeground(Color.BLACK);
        combo.setBackground(Color.WHITE);
        combo.setFont(FUENTE_NORMAL);
        combo.setOpaque(true);
        aplicarRendererCombo(combo);
    }

    private static void aplicarRendererCombo(JComboBox<?> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {

                // Llamamos al renderizador por defecto para obtener el JLabel base
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // Forzamos la fuente y colores
                label.setFont(FUENTE_NORMAL);

                if (isSelected) {
                    // Color cuando pasas el mouse o seleccionas
                    label.setBackground(new Color(218, 230, 250)); // Azul suave
                    label.setForeground(Color.BLACK);              // LETRA NEGRA (Solución)
                } else {
                    // Color de fondo normal
                    label.setBackground(Color.WHITE);              // FONDO BLANCO (Solución)
                    label.setForeground(Color.BLACK);              // LETRA NEGRA (Solución)
                }

                // Esto asegura que el label sea opaco y se pinte correctamente
                label.setOpaque(true);
                return label;
            }
        });
    }
    public static JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setGridColor(BORDE);

        // Configuraciones base de la tabla (Fondo blanco, letra negra)
        tabla.setForeground(Color.BLACK);
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(new Color(218, 230, 250));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setOpaque(true);
        tabla.setFillsViewportHeight(true);

        // 1. CONFIGURAR EL ENCABEZADO Y BLOQUEAR MOVIMIENTO
        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false); // Bloquea que se arrastren las columnas

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                label.setBackground(AZUL_EPN);       // Forzar fondo AZUL MARINO
                label.setForeground(Color.WHITE);    // Forzar letra BLANCA
                label.setFont(new Font("Arial", Font.BOLD, 12));
                label.setHorizontalAlignment(SwingConstants.CENTER);

                // Le ponemos un pequeño borde a la derecha e inferior para separar visualmente las columnas
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BORDE));
                label.setOpaque(true);
                return label;
            }
        };

        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.PLAIN, 12));

                if (isSelected) {
                    label.setBackground(new Color(218, 230, 250)); // Azul clarito al hacer clic
                    label.setForeground(Color.BLACK);              // Letra negra
                } else {
                    label.setBackground(Color.WHITE);              // Fondo blanco
                    label.setForeground(Color.BLACK);              // Letra negra
                }

                label.setOpaque(true);
                return label;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }

        return tabla;
    }

    public static JTextArea crearAreaTexto() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        return area;
    }

    public static JPanel crearTarjeta() {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                new EmptyBorder(12, 12, 12, 12)));
        return tarjeta;
    }

    public static JPanel crearTarjetaIndicador(String titulo, JLabel valor) {
        JPanel tarjeta = crearTarjeta();
        tarjeta.setLayout(new BorderLayout(5, 5));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(88, 96, 116));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        valor.setForeground(AZUL_CLARO);
        valor.setFont(new Font("Arial", Font.BOLD, 23));
        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);
        return tarjeta;
    }

    public static JPanel crearPanelContenido(String titulo, String subtitulo) {
        JPanel externo = new JPanel(new BorderLayout(10, 10));
        externo.setBackground(FONDO);
        externo.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(AZUL_EPN);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setForeground(new Color(92, 101, 120));
        lblSubtitulo.setFont(FUENTE_NORMAL);
        cabecera.add(lblTitulo, BorderLayout.NORTH);
        cabecera.add(lblSubtitulo, BorderLayout.SOUTH);
        externo.add(cabecera, BorderLayout.NORTH);
        return externo;
    }
}