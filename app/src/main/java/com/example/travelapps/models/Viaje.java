package com.example.travelapps.models;

import java.io.Serializable;
import java.util.Date;

public class Viaje implements Serializable {
    private int idViaje;
    private int idUsuario;
    private String nombre;
    private String descripcion;
    private  Date fechaInicio;
    private Date fechaFin;

    // Getters y setters
    public int getIdViaje() { return idViaje; }
    public void setIdViaje(int idViaje) { this.idViaje = idViaje; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }


    @Override
    public String toString() {
        return "Viaje{" +
                "idViaje=" + idViaje +
                ", nombre='" + nombre + '\'' +
                ", idusuario='" + idUsuario + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                '}';
    }
}