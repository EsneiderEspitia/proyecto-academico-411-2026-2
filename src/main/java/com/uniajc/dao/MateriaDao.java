package com.uniajc.dao;

import com.uniajc.db.ConexionDatabase;
import com.uniajc.modelo.Materia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriaDao {
    private static final String TABLA = "\"practica-mvc\".materias";
    private static final String COLUMNAS = "id, name, credits";

    public void guardar(Materia materia) {
        String sql = "INSERT INTO " + TABLA + " (name, credits) VALUES (?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, materia.getNombre().trim());
            statement.setInt(2, materia.getCreditos());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    materia.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar la materia.", e);
        }
    }

    public List<Materia> obtenerTodos() {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " ORDER BY id";
        List<Materia> materias = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                materias.add(mapear(resultSet));
            }
            return materias;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar las materias.", e);
        }
    }

    public Optional<Materia> buscarPorId(int id) {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapear(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo consultar la materia.", e);
        }
    }

    public void actualizar(Materia materia) {
        String sql = "UPDATE " + TABLA + " SET name = ?, credits = ? WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, materia.getNombre().trim());
            statement.setInt(2, materia.getCreditos());
            statement.setInt(3, materia.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe una materia con ID " + materia.getId() + ".");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar la materia.", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe una materia con ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo eliminar la materia.", e);
        }
    }

    private Connection connection() {
        Connection connection = ConexionDatabase.getConnection();
        if (connection == null) {
            throw new IllegalStateException("No hay conexión disponible con la base de datos.");
        }
        return connection;
    }

    private Materia mapear(ResultSet resultSet) throws SQLException {
        return new Materia(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("credits"));
    }
}
