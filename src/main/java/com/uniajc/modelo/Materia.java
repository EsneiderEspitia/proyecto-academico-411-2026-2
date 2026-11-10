package com.uniajc.modelo;

public class Materia {
    private int id;
    private String nombre;
    private int creditos;

    public Materia() {
    }

    public Materia(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMateria() {
        return id;
    }

    public void setIdMateria(int idMateria) {
        this.id = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreMateria() {
        return nombre;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombre = nombreMateria;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}
