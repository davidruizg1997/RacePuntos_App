package com.example.davidruiz.preliminar;

/**
 * Created by DavidRuiz on 23/02/2018.
 */

public class Premios {
    private String Titulo;
    private int Puntos;
    private String Descripcion;
    private int Miniatura;

    public Premios() {
    }

    public Premios(String titulo, int puntos, String descripcion, int miniatura) {
        Titulo = titulo;
        Puntos = puntos;
        Descripcion = descripcion;
        Miniatura = miniatura;
    }

    public String getTitulo() {
        return Titulo;
    }

    public int getPuntos() {
        return Puntos;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public int getMiniatura() {
        return Miniatura;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public void setPuntos(int puntos) {
        Puntos = puntos;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setMiniatura(int miniatura) {
        Miniatura = miniatura;
    }
}
