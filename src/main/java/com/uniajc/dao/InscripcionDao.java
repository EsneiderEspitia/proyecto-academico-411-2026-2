package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniajc.db.ConexionDatabase;
import com.uniajc.modelo.Inscripcion;

/**
 * Data Access Object para la entidad Inscripcion.
 * Opera sobre la tabla "practica-mvc".inscripciones con JDBC.
 */
public class InscripcionDao {

    public void guardar(Inscripcion inscripcion) {
        String sql = "INSERT INTO \"practica-mvc\".inscripciones (id_estudiante, id_grupo, final_grade, status) " +
                     "VALUES (?, ?, ?, ?)";

        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) {
                throw new RuntimeException("No se pudo obtener la conexión a la base de datos.");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, inscripcion.getIdEstudiante());
                pstmt.setInt(2, inscripcion.getIdGrupo());

                if (inscripcion.getFinalGrade() != null) {
                    pstmt.setDouble(3, inscripcion.getFinalGrade());
                } else {
                    pstmt.setNull(3, Types.DECIMAL);
                }

                String status = (inscripcion.getStatus() != null && !inscripcion.getStatus().trim().isEmpty())
                        ? inscripcion.getStatus().trim().toUpperCase()
                        : "ACTIVO";
                pstmt.setString(4, status);

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        inscripcion.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar la inscripción: " + e.getMessage(), e);
        }
    }

    public List<Inscripcion> obtenerTodos() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, i.created_at, " +
                     "       CONCAT(e.name, ' ', e.lastname) AS nombre_estudiante, " +
                     "       CONCAT(m.name, ' - ', g.classroom, ' (', g.schedule, ')') AS detalle_grupo " +
                     "FROM \"practica-mvc\".inscripciones i " +
                     "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                     "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                     "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                     "ORDER BY i.id ASC";

        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) {
                return lista;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEntidad(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener inscripciones: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Inscripcion> buscarPorId(int id) {
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, i.created_at, " +
                     "       CONCAT(e.name, ' ', e.lastname) AS nombre_estudiante, " +
                     "       CONCAT(m.name, ' - ', g.classroom, ' (', g.schedule, ')') AS detalle_grupo " +
                     "FROM \"practica-mvc\".inscripciones i " +
                     "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                     "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                     "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                     "WHERE i.id = ?";

        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) {
                return Optional.empty();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar inscripción por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Inscripcion> buscarPorEstudianteYGrupo(int idEstudiante, int idGrupo) {
        String sql = "SELECT id, id_estudiante, id_grupo, final_grade, status, created_at " +
                     "FROM \"practica-mvc\".inscripciones " +
                     "WHERE id_estudiante = ? AND id_grupo = ?";

        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) {
                return Optional.empty();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idEstudiante);
                pstmt.setInt(2, idGrupo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Inscripcion inscripcion = new Inscripcion();
                        inscripcion.setId(rs.getInt("id"));
                        inscripcion.setIdEstudiante(rs.getInt("id_estudiante"));
                        inscripcion.setIdGrupo(rs.getInt("id_grupo"));
                        double nota = rs.getDouble("final_grade");
                        if (!rs.wasNull()) {
                            inscripcion.setFinalGrade(nota);
                        }
                        inscripcion.setStatus(rs.getString("status"));
                        inscripcion.setCreatedAt(rs.getTimestamp("created_at"));
                        return Optional.of(inscripcion);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al validar duplicidad de inscripción: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Inscripcion inscripcion) {
        String sql = "UPDATE \"practica-mvc\".inscripciones " +
                     "SET id_estudiante = ?, id_grupo = ?, final_grade = ?, status = ? " +
                     "WHERE id = ?";

        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) {
                throw new RuntimeException("No se pudo obtener la conexión a la base de datos.");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, inscripcion.getIdEstudiante());
                pstmt.setInt(2, inscripcion.getIdGrupo());

                if (inscripcion.getFinalGrade() != null) {
                    pstmt.setDouble(3, inscripcion.getFinalGrade());
                } else {
                    pstmt.setNull(3, Types.DECIMAL);
                }

                String status = (inscripcion.getStatus() != null && !inscripcion.getStatus().trim().isEmpty())
                        ? inscripcion.getStatus().trim().toUpperCase()
                        : "ACTIVO";
                pstmt.setString(4, status);
                pstmt.setInt(5, inscripcion.getId());

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Inscripción no encontrada con ID: " + inscripcion.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar inscripción: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".inscripciones WHERE id = ?";

        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) {
                throw new RuntimeException("No se pudo obtener la conexión a la base de datos.");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Inscripción no encontrada con ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar inscripción: " + e.getMessage(), e);
        }
    }

    public boolean existeEstudiante(int idEstudiante) {
        String sql = "SELECT 1 FROM \"practica-mvc\".estudiantes WHERE id = ?";
        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idEstudiante);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean existeGrupo(int idGrupo) {
        String sql = "SELECT 1 FROM \"practica-mvc\".grupos WHERE id = ?";
        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idGrupo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Map<String, Object>> obtenerEstudiantesOpciones() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes ORDER BY name ASC";
        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) return lista;
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("nombre", rs.getString("name") + " " + rs.getString("lastname"));
                    map.put("email", rs.getString("email"));
                    lista.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar estudiantes para combo: " + e.getMessage());
        }
        return lista;
    }

    public List<Map<String, Object>> obtenerGruposOpciones() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT g.id, m.name AS materia, g.classroom, g.schedule " +
                     "FROM \"practica-mvc\".grupos g " +
                     "JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                     "ORDER BY m.name ASC";
        try {
            Connection conn = ConexionDatabase.getConnection();
            if (conn == null) return lista;
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("descripcion", rs.getString("materia") + " - Aula: " + rs.getString("classroom") + " (" + rs.getString("schedule") + ")");
                    lista.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar grupos para combo: " + e.getMessage());
        }
        return lista;
    }

    private Inscripcion mapearEntidad(ResultSet rs) throws SQLException {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(rs.getInt("id"));
        inscripcion.setIdEstudiante(rs.getInt("id_estudiante"));
        inscripcion.setIdGrupo(rs.getInt("id_grupo"));

        double nota = rs.getDouble("final_grade");
        if (!rs.wasNull()) {
            inscripcion.setFinalGrade(nota);
        } else {
            inscripcion.setFinalGrade(null);
        }

        inscripcion.setStatus(rs.getString("status"));
        inscripcion.setCreatedAt(rs.getTimestamp("created_at"));

        try {
            inscripcion.setNombreEstudiante(rs.getString("nombre_estudiante"));
            inscripcion.setDetalleGrupo(rs.getString("detalle_grupo"));
        } catch (SQLException ignored) {
            // Campos opcionales de JOIN
        }

        return inscripcion;
    }
}

