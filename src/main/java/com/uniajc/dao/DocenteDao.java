package com.uniajc.dao;

import com.uniajc.db.ConexionDatabase;
import com.uniajc.modelo.Docente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocenteDao {
    private static final String TABLA = "\"practica-mvc\".docentes";
    private static final String COLUMNAS = "id, name, specialty, email";

    public void guardar(Docente docente) {
        String sql = "INSERT INTO " + TABLA + " (name, specialty, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, docente.getNombre().trim());
            statement.setString(2, docente.getEspecialidad().trim());
            statement.setString(3, docente.getEmail().trim());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    docente.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar el docente.", e);
        }
    }

    public List<Docente> obtenerTodos() {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " ORDER BY id";
        List<Docente> docentes = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                docentes.add(mapear(resultSet));
            }
            return docentes;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar los docentes.", e);
        }
    }

    public Optional<Docente> buscarPorId(int id) {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapear(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo consultar el docente.", e);
        }
    }

    public Optional<Docente> buscarPorEmail(String email) {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " WHERE LOWER(email) = LOWER(?)";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, email.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapear(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo consultar el docente por email.", e);
        }
    }

    public void actualizar(Docente docente) {
        String sql = "UPDATE " + TABLA + " SET name = ?, specialty = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, docente.getNombre().trim());
            statement.setString(2, docente.getEspecialidad().trim());
            statement.setString(3, docente.getEmail().trim());
            statement.setInt(4, docente.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe un docente con ID " + docente.getId() + ".");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el docente.", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe un docente con ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo eliminar el docente.", e);
        }
    }

    private Connection connection() {
        Connection connection = ConexionDatabase.getConnection();
        if (connection == null) {
            throw new IllegalStateException("No hay conexión disponible con la base de datos.");
        }
        return connection;
    }

    private Docente mapear(ResultSet resultSet) throws SQLException {
        return new Docente(resultSet.getInt("id"), resultSet.getString("name"),
                resultSet.getString("specialty"), resultSet.getString("email"));
    }
}
