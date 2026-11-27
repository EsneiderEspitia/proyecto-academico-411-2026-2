package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * Ventana Principal / Dashboard del Sistema de Gestión Académica UNIAJC.
 * Integra la navegación modular de las 5 entidades mediante JTabbedPane.
 */
public class VistaPrincipalSwing extends JFrame {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);

    private JTabbedPane pestañasModulos;
    private VistaInscripcionSwing vistaInscripcion;

    public VistaPrincipalSwing() {
        configurarLookAndFeel();
        initComponents();
    }

    private void configurarLookAndFeel() {
        try {
            UIManager.put("Label.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
            UIManager.put("Button.font", new FontUIResource("Segoe UI", Font.BOLD, 12));
            UIManager.put("Table.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
            UIManager.put("TabbedPane.font", new FontUIResource("Segoe UI", Font.BOLD, 13));
        } catch (Exception ignored) {}
    }

    private void initComponents() {
        setTitle("Sistema de Gestión Académica UNIAJC - Práctica 411");
        setSize(1000, 700);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(COLOR_SECUNDARIO);

        // 1. Encabezado Institucional
        panelContenedor.add(crearEncabezado(), BorderLayout.NORTH);

        // 2. Pestañas de Módulos (JTabbedPane)
        pestañasModulos = new JTabbedPane();
        pestañasModulos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestañasModulos.setBackground(Color.WHITE);
        pestañasModulos.setForeground(COLOR_PRIMARIO);

        // Módulo 1: Estudiantes (Asignado a Persona 1)
        pestañasModulos.addTab("👨‍🎓 Estudiantes", crearPanelPlaceholder(
                "Módulo de Estudiantes",
                "Gestión de Alumnos, datos personales y correos institucionales.",
                "Persona 1 (En desarrollo colaborativo)"
        ));

        // Módulo 2: Docentes (Asignado a Persona 2)
        pestañasModulos.addTab("👨‍🏫 Docentes", crearPanelPlaceholder(
                "Módulo de Docentes",
                "Gestión de Profesores y Áreas de Especialidad.",
                "Persona 2 (En desarrollo colaborativo)"
        ));

        // Módulo 3: Materias (Asignado a Persona 2)
        pestañasModulos.addTab("📚 Materias", crearPanelPlaceholder(
                "Módulo de Materias",
                "Catálogo de Asignaturas y Créditos Académicos (1 a 10).",
                "Persona 2 (En desarrollo colaborativo)"
        ));

        // Módulo 4: Grupos (Asignado a Persona 3)
        pestañasModulos.addTab("👥 Grupos", crearPanelPlaceholder(
                "Módulo de Grupos",
                "Oferta Académica articulada entre Materia, Docente, Aula y Horario.",
                "Persona 3 (En desarrollo colaborativo)"
        ));

        // Módulo 5: Inscripciones (Desarrollado 100% por Persona 4 - Esneider Espitia)
        vistaInscripcion = new VistaInscripcionSwing();
        pestañasModulos.addTab("📝 Inscripciones", vistaInscripcion);

        // Seleccionar por defecto la pestaña de Inscripciones para pruebas inmediatas
        pestañasModulos.setSelectedComponent(vistaInscripcion);

        panelContenedor.add(pestañasModulos, BorderLayout.CENTER);

        // 3. Barra de Estado
        panelContenedor.add(crearBarraEstado(), BorderLayout.SOUTH);

        setContentPane(panelContenedor);
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel labelInstitucion = new JLabel("INSTITUCIÓN UNIVERSITARIA ANTONIO JOSÉ CAMACHO");
        labelInstitucion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelInstitucion.setForeground(Color.WHITE);
        labelInstitucion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelSubtitulo = new JLabel("Sistema de Gestión Académica - Práctica MVC + PostgreSQL (PA411)");
        labelSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelSubtitulo.setForeground(new Color(236, 240, 241));
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(labelInstitucion);
        header.add(Box.createVerticalStrut(4));
        header.add(labelSubtitulo);

        return header;
    }

    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        barra.setBackground(new Color(52, 73, 94));

        JLabel labelEstado = new JLabel("🟢 Base de Datos: Supabase PostgreSQL (Activa)  |  Esquema: practica-mvc  |  Rama: Espitia (Persona 4)");
        labelEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelEstado.setForeground(Color.WHITE);

        barra.add(labelEstado);
        return barra;
    }

    private JPanel crearPanelPlaceholder(String titulo, String descripcion, String responsable) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_SECUNDARIO);

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(descripcion, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(COLOR_TEXTO);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblResp = new JLabel("Asignado a: " + responsable, SwingConstants.CENTER);
        lblResp.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblResp.setForeground(new Color(127, 140, 141));
        lblResp.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblDesc);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(lblResp);

        panel.add(tarjeta);
        return panel;
    }

    // Métodos para permitir que los compañeros conecten sus vistas al hacer merge
    public void setPanelEstudiantes(JPanel panel) {
        pestañasModulos.setComponentAt(0, panel);
    }

    public void setPanelDocentes(JPanel panel) {
        pestañasModulos.setComponentAt(1, panel);
    }

    public void setPanelMaterias(JPanel panel) {
        pestañasModulos.setComponentAt(2, panel);
    }

    public void setPanelGrupos(JPanel panel) {
        pestañasModulos.setComponentAt(3, panel);
    }

    public VistaInscripcionSwing getVistaInscripcion() {
        return vistaInscripcion;
    }
}

