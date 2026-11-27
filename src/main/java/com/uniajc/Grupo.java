package com.uniajc;

public class Grupo {
    private int id_grupo;
    private int id_materia;
    private int id_docente;
    private String aula;
    private String horario;

    // Constructor vacío
    public Grupo() {
    }

    // Constructor con todos los atributos
    public Grupo(int id_grupo, int id_materia, int id_docente, String aula, String horario) {
        this.id_grupo = id_grupo;
        this.id_materia = id_materia;
        this.id_docente = id_docente;
        this.aula = aula;
        this.horario = horario;
    }

    // Getters y Setters
    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public int getId_materia() {
        return id_materia;
    }

    public void setId_materia(int id_materia) {
        this.id_materia = id_materia;
    }

    public int getId_docente() {
        return id_docente;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    // Método toString para visualizar los datos fácilmente
    @Override
    public String toString() {
        return "Grupo [ID: " + id_grupo + ", Materia: " + id_materia + ", Docente: " + id_docente + 
               ", Aula: " + aula + ", Horario: " + horario + "]";
    }
}
