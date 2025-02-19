package com.example.travelapps.models;

public class Foto {

    private int idFoto;
    private int idViaje;
    private String url;

    public Foto() {
    }

    public Foto(int idFoto, int idViaje, String url) {
        this.idFoto = idFoto;
        this.idViaje = idViaje;
        this.url = url;
    }

    public int getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(int idFoto) {
        this.idFoto = idFoto;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Foto{" +
                "idFoto=" + idFoto +
                ", idViaje=" + idViaje +
                ", url='" + url + '\'' +
                '}';
    }
}
