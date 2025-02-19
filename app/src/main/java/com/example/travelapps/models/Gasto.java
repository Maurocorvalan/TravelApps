package com.example.travelapps.models;

public class Gasto {
    private int idGasto;
    private int idViaje;

    private String categoria;
    private double monto;
    private String descripcion;

    public int getIdGasto() {
        return idGasto;
    }

    public Gasto() {
    }

    public Gasto(String categoria, double monto, String descripcion) {
        this.categoria = categoria;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public void setIdGasto(int idGasto) {
        this.idGasto = idGasto;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Gasto{" +
                "idGasto=" + idGasto +
                ", idViaje=" + idViaje +
                ", categoria='" + categoria + '\'' +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
