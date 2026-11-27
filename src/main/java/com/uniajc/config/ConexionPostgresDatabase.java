package com.uniajc.config;

import java.sql.Connection;
import com.uniajc.db.ConexionDatabase;

/**
 * Clase adaptadora de configuración para compatibilidad con la guía de implementación.
 * Delega la obtención de la conexión JDBC a ConexionDatabase.
 */
public class ConexionPostgresDatabase {

    public static Connection getConnection() {
        return ConexionDatabase.getConnection();
    }

    public static void closeConnection() {
        ConexionDatabase.closeConnection();
    }
}

