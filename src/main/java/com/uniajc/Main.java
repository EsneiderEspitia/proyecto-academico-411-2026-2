package com.uniajc;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.uniajc.db.ConexionDatabase;
import com.uniajc.vista.VistaPrincipalSwing;

/**
 * Punto de entrada principal para la aplicación del Sistema de Gestión Académica UNIAJC.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  SISTEMA DE GESTIÓN ACADÉMICA - UNIAJC (PA411)   ");
        System.out.println("  Arquitectura MVC + Base de Datos PostgreSQL    ");
        System.out.println("==================================================");

        // Probar conexión a base de datos al inicio
        System.out.println("Verificando conexión con PostgreSQL (Supabase)...");
        ConexionDatabase.getConnection();

        // Configurar Look & Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Iniciar la interfaz gráfica Swing en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            VistaPrincipalSwing ventana = new VistaPrincipalSwing();
            ventana.setVisible(true);
        });
    }
}
