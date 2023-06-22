package com.example.ruhe;

public class Ruta {
    private String nombreRuta;
    private String origen;
    private String destino;
    private String pregunta;
    private String tiempo;
    private String contacto;


    public Ruta(String nombreRuta, String origen, String destino, String pregunta, String tiempo, String contacto) {
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
        this.pregunta = pregunta;
        this.tiempo = tiempo;
        this.contacto = contacto;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getContacto() {
        return contacto;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public String getTiempo() {
        return tiempo;
    }

}