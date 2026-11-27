package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.servicios.InscripcionService;

/**
 * Vista interactiva Swing para la gestión académica de Inscripciones y Calificaciones.
 * Sigue la guía de diseño institucional de la UNIAJC.
 */
public class VistaInscripcionSwing extends JPanel implements ActionListener {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);

    private static final String[] COLUMNAS = {
            "ID", "ID Estudiante", "Estudiante", "ID Grupo", "Grupo / Materia", "Nota Final", "Estado"
    };

    private final InscripcionService inscripcionService;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar;
    private JButton botonRegistrar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    // Componentes del modal
    private JDialog dialogoFormulario;
    private JComboBox<ComboItem> comboEstudiantes;
    private JComboBox<ComboItem> comboGrupos;
    private JTextField campoNotaFinal;
    private JComboBox<String> comboEstado;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private Inscripcion inscripcionSeleccionada;

    public VistaInscripcionSwing() {
        this.inscripcionService = new InscripcionService();
        initComponents();
        cargarInscripciones();
    }

    public VistaInscripcionSwing(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
        initComponents();
        cargarInscripciones();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_SECUNDARIO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(COLOR_SECUNDARIO);

        JLabel labelTitulo = new JLabel("📝 Gestión de Inscripciones y Calificaciones");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        labelTitulo.setForeground(COLOR_PRIMARIO);

        JLabel labelBusqueda = new JLabel("Filtrar:");
        labelBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelBusqueda.setForeground(COLOR_TEXTO);

        textoBusqueda = new JTextField(22);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(200, 32));

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscarInscripciones());

        panel.add(labelTitulo);
        panel.add(new JLabel("   |   "));
        panel.add(labelBusqueda);
        panel.add(textoBusqueda);
        panel.add(botonBuscar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_SECUNDARIO);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(28);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setGridColor(new Color(189, 195, 199));
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(new Color(174, 214, 241));
        tabla.setSelectionForeground(COLOR_TEXTO);

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        scrollPane.setPreferredSize(new Dimension(800, 380));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(COLOR_SECUNDARIO);

        botonRegistrar = crearBoton("➕ Inscribir Alumno", COLOR_EXITO);
        botonRegistrar.addActionListener(e -> mostrarDialogoRegistro(false));

        botonEditar = crearBoton("✏ Calificar / Editar", COLOR_PRIMARIO);
        botonEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() >= 0) {
                mostrarDialogoRegistro(true);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una inscripción de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBoton("🗑 Eliminar", COLOR_ACENTO);
        botonEliminar.addActionListener(e -> eliminarInscripcion());

        botonActualizar = crearBoton("🔄 Refrescar", new Color(149, 165, 166));
        botonActualizar.addActionListener(e -> cargarInscripciones());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
        panel.add(botonActualizar);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(150, 35));
        return boton;
    }

    public void cargarInscripciones() {
        modeloTabla.setRowCount(0);
        try {
            List<Inscripcion> lista = inscripcionService.obtenerTodasLasInscripciones();
            for (Inscripcion ins : lista) {
                String notaStr = (ins.getFinalGrade() != null) ? String.format("%.2f", ins.getFinalGrade()) : "Sin Calificar";
                modeloTabla.addRow(new Object[]{
                        ins.getId(),
                        ins.getIdEstudiante(),
                        (ins.getNombreEstudiante() != null) ? ins.getNombreEstudiante() : "Estudiante #" + ins.getIdEstudiante(),
                        ins.getIdGrupo(),
                        (ins.getDetalleGrupo() != null) ? ins.getDetalleGrupo() : "Grupo #" + ins.getIdGrupo(),
                        notaStr,
                        ins.getStatus()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar inscripciones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarInscripciones() {
        modeloTabla.setRowCount(0);
        String busqueda = textoBusqueda.getText().trim().toLowerCase();

        if (busqueda.isEmpty()) {
            cargarInscripciones();
            return;
        }

        try {
            List<Inscripcion> lista = inscripcionService.obtenerTodasLasInscripciones();
            for (Inscripcion ins : lista) {
                String estudiante = (ins.getNombreEstudiante() != null) ? ins.getNombreEstudiante().toLowerCase() : "";
                String grupo = (ins.getDetalleGrupo() != null) ? ins.getDetalleGrupo().toLowerCase() : "";
                String estado = (ins.getStatus() != null) ? ins.getStatus().toLowerCase() : "";

                boolean coincide = String.valueOf(ins.getId()).contains(busqueda) ||
                                   estudiante.contains(busqueda) ||
                                   grupo.contains(busqueda) ||
                                   estado.contains(busqueda);

                if (coincide) {
                    String notaStr = (ins.getFinalGrade() != null) ? String.format("%.2f", ins.getFinalGrade()) : "Sin Calificar";
                    modeloTabla.addRow(new Object[]{
                            ins.getId(),
                            ins.getIdEstudiante(),
                            (ins.getNombreEstudiante() != null) ? ins.getNombreEstudiante() : "Estudiante #" + ins.getIdEstudiante(),
                            ins.getIdGrupo(),
                            (ins.getDetalleGrupo() != null) ? ins.getDetalleGrupo() : "Grupo #" + ins.getIdGrupo(),
                            notaStr,
                            ins.getStatus()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al filtrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro(boolean esEdicion) {
        modoEdicion = esEdicion;

        if (esEdicion && tabla.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            try {
                inscripcionSeleccionada = inscripcionService.buscarInscripcionPorId(id).orElse(null);
                if (inscripcionSeleccionada == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró el registro seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            inscripcionSeleccionada = new Inscripcion();
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        dialogoFormulario = new JDialog((JFrame) (parentWindow instanceof JFrame ? parentWindow : null),
                esEdicion ? "✏ Calificar / Editar Inscripción" : "➕ Matricular Estudiante en Grupo", true);
        dialogoFormulario.setSize(520, 360);
        dialogoFormulario.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel labelTitulo = new JLabel(esEdicion ? "✏ Actualizar Calificación / Inscripción" : "➕ Nueva Matrícula Académica");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelTitulo.setForeground(COLOR_PRIMARIO);
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Combo Estudiantes
        JLabel labelEstudiante = new JLabel("Estudiante:");
        labelEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        panelFormulario.add(labelEstudiante, gbc);

        comboEstudiantes = new JComboBox<>();
        cargarComboEstudiantes();
        gbc.gridx = 1;
        panelFormulario.add(comboEstudiantes, gbc);

        gbc.gridy++;

        // Combo Grupos
        JLabel labelGrupo = new JLabel("Grupo Ofertado:");
        labelGrupo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        panelFormulario.add(labelGrupo, gbc);

        comboGrupos = new JComboBox<>();
        cargarComboGrupos();
        gbc.gridx = 1;
        panelFormulario.add(comboGrupos, gbc);

        gbc.gridy++;

        // Campo Nota Final
        JLabel labelNota = new JLabel("Nota Final (0.0 - 5.0):");
        labelNota.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        panelFormulario.add(labelNota, gbc);

        campoNotaFinal = new JTextField(10);
        campoNotaFinal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoNotaFinal, gbc);

        gbc.gridy++;

        // Combo Estado
        JLabel labelEstado = new JLabel("Estado:");
        labelEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0;
        panelFormulario.add(labelEstado, gbc);

        comboEstado = new JComboBox<>(new String[]{"ACTIVO", "APROBADO", "REPROBADO", "CANCELADO"});
        gbc.gridx = 1;
        panelFormulario.add(comboEstado, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;

        // Botones del formulario
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        botonGuardar = crearBoton("Guardar Matrícula", COLOR_EXITO);
        botonGuardar.addActionListener(e -> guardarInscripcion());

        JButton botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelFormulario.add(panelBotones, gbc);

        // Si es edición, precargar datos
        if (esEdicion && inscripcionSeleccionada != null) {
            seleccionarEnCombo(comboEstudiantes, inscripcionSeleccionada.getIdEstudiante());
            seleccionarEnCombo(comboGrupos, inscripcionSeleccionada.getIdGrupo());
            if (inscripcionSeleccionada.getFinalGrade() != null) {
                campoNotaFinal.setText(String.valueOf(inscripcionSeleccionada.getFinalGrade()));
            }
            comboEstado.setSelectedItem(inscripcionSeleccionada.getStatus());
        }

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void cargarComboEstudiantes() {
        comboEstudiantes.removeAllItems();
        List<Map<String, Object>> estudiantes = inscripcionService.obtenerEstudiantesDisponibles();
        for (Map<String, Object> map : estudiantes) {
            int id = (int) map.get("id");
            String texto = id + " - " + map.get("nombre");
            comboEstudiantes.addItem(new ComboItem(id, texto));
        }
    }

    private void cargarComboGrupos() {
        comboGrupos.removeAllItems();
        List<Map<String, Object>> grupos = inscripcionService.obtenerGruposDisponibles();
        for (Map<String, Object> map : grupos) {
            int id = (int) map.get("id");
            String texto = id + " - " + map.get("descripcion");
            comboGrupos.addItem(new ComboItem(id, texto));
        }
    }

    private void seleccionarEnCombo(JComboBox<ComboItem> combo, int idBuscado) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == idBuscado) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardarInscripcion() {
        try {
            ComboItem estudianteItem = (ComboItem) comboEstudiantes.getSelectedItem();
            ComboItem grupoItem = (ComboItem) comboGrupos.getSelectedItem();

            if (estudianteItem == null || grupoItem == null) {
                JOptionPane.showMessageDialog(dialogoFormulario, "Debe seleccionar un estudiante y un grupo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            inscripcionSeleccionada.setIdEstudiante(estudianteItem.getId());
            inscripcionSeleccionada.setIdGrupo(grupoItem.getId());

            String notaTexto = campoNotaFinal.getText().trim();
            if (!notaTexto.isEmpty()) {
                try {
                    double nota = Double.parseDouble(notaTexto.replace(",", "."));
                    inscripcionSeleccionada.setFinalGrade(nota);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(dialogoFormulario, "La calificación debe ser un valor numérico (ej: 4.5).", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                inscripcionSeleccionada.setFinalGrade(null);
            }

            String estadoSeleccionado = (String) comboEstado.getSelectedItem();
            inscripcionSeleccionada.setStatus(estadoSeleccionado);

            if (modoEdicion) {
                inscripcionService.actualizarInscripcion(inscripcionSeleccionada);
                JOptionPane.showMessageDialog(dialogoFormulario, "Inscripción actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                inscripcionService.registrarInscripcion(inscripcionSeleccionada);
                JOptionPane.showMessageDialog(dialogoFormulario, "Estudiante matriculado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarInscripciones();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarInscripcion() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la inscripción seleccionada?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                inscripcionService.eliminarInscripcion(id);
                JOptionPane.showMessageDialog(this, "Inscripción eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarInscripciones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    /**
     * Clase auxiliar interna para poblar los selectores JComboBox con par (ID, Texto).
     */
    private static class ComboItem {
        private final int id;
        private final String texto;

        public ComboItem(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return texto;
        }
    }
}

