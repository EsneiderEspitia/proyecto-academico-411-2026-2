package com.uniajc;

import com.uniajc.db.ConexionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {

    // Método para listar todos los grupos desde Supabase
    public List<Grupo> obtenerGrupos() {
        List<Grupo> listaGrupos = new ArrayList<>();
        String sql = "SELECT id_grupo, id_materia, id_docente, aula, horario FROM grupo";

        // Usamos getConnection() tal como está definido en ConexionDatabase
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idGrupo = rs.getInt("id_grupo");
                int idMateria = rs.getInt("id_materia");
                int idDocente = rs.getInt("id_docente");
                String aula = rs.getString("aula");
                String horario = rs.getString("horario");

                Grupo grupo = new Grupo(idGrupo, idMateria, idDocente, aula, horario);
                listaGrupos.add(grupo);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al consultar los grupos: " + e.getMessage());
        }

        return listaGrupos;
    }

    // Método para insertar un nuevo grupo
    public void insertarGrupo(Grupo grupo) {
        String sql = "INSERT INTO grupo (id_materia, id_docente, aula, horario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grupo.getId_materia());
            pstmt.setInt(2, grupo.getId_docente());
            pstmt.setString(3, grupo.getAula());
            pstmt.setString(4, grupo.getHorario());

            pstmt.executeUpdate();
            System.out.println("✅ ¡Grupo registrado con éxito en Supabase!");

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar el grupo: " + e.getMessage());
        }
    }
}