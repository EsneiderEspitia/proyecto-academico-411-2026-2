package com.uniajc.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor de conexión JDBC con patrón Singleton hacia la base de datos PostgreSQL.
 */
public class ConexionDatabase {

    private static Connection connection = null;

    /**
     * Obtiene la conexión a la base de datos. Si no existe o está cerrada,
     * la crea cargando los parámetros desde config.properties.
     *
     * @return Objeto Connection listo para usar, o null si falla.
     */
    public static synchronized Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Properties properties = new Properties();
            InputStream input = null;

            File configFile = new File("config.properties");
            if (configFile.exists()) {
                input = new FileInputStream(configFile);
            } else {
                input = ConexionDatabase.class.getClassLoader().getResourceAsStream("config.properties");
            }

            if (input == null) {
                return null;
            }

            properties.load(input);
            input.close();

            String url = properties.getProperty("URL");
            String user = properties.getProperty("USERNAME");
            String password = properties.getProperty("PASSWORD");

            if (url == null || user == null) {
                return null;
            }

            // Registrar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Timeout rápido para evitar bloqueos
            DriverManager.setLoginTimeout(3);

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a PostgreSQL.");

        } catch (SQLException error) {
            System.err.println("Aviso JDBC: " + error.getMessage());
            connection = null;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL no encontrado: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println("Archivo config.properties no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al leer config.properties: " + e.getMessage());
        }

        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Conexión cerrada exitosamente.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
