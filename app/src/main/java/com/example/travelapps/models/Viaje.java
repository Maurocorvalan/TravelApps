package com.example.travelapps.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Viaje implements Serializable {
    private int idViaje;
    private int idUsuario;
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
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

    // Métodos para obtener las fechas como String
    public String getFechaInicioString() {
        if (fechaInicio != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.format(fechaInicio);
        }
        return null;
    }

    public String getFechaFinString() {
        if (fechaFin != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.format(fechaFin);
        }
        return null;
    }

    // Métodos para establecer las fechas a partir de un String
    public void setFechaInicioString(String fechaInicioStr) {
        if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.fechaInicio = dateFormat.parse(fechaInicioStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFechaFinString(String fechaFinStr) {
        if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.fechaFin = dateFormat.parse(fechaFinStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

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