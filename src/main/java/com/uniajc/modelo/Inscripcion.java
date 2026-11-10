package com.uniajc.modelo;

import java.sql.Timestamp;

/**
 * Entidad que representa una inscripción académica de un estudiante en un grupo.
 * Coincide con la tabla "practica-mvc".inscripciones.
 */
public class Inscripcion {

    private int id;
    private int idEstudiante;
    private int idGrupo;
    private Double finalGrade;
    private String status;
    private Timestamp createdAt;

    // Atributos informativos para facilitar la visualización en tablas y vistas
    private String nombreEstudiante;
    private String detalleGrupo;

    public Inscripcion() {
        this.status = "ACTIVO";
    }

    public Inscripcion(int idEstudiante, int idGrupo) {
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.status = "ACTIVO";
    }

    public Inscripcion(int id, int idEstudiante, int idGrupo, Double finalGrade, String status) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.finalGrade = finalGrade;
        this.status = status;
    }

    public Inscripcion(int id, int idEstudiante, int idGrupo, Double finalGrade, String status, Timestamp createdAt) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.finalGrade = finalGrade;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getDetalleGrupo() {
        return detalleGrupo;
    }

    public void setDetalleGrupo(String detalleGrupo) {
        this.detalleGrupo = detalleGrupo;
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", idEstudiante=" + idEstudiante +
                ", idGrupo=" + idGrupo +
                ", finalGrade=" + finalGrade +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

