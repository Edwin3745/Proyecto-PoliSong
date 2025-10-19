package com.polisong.polisong_marketplace.model;


import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
@Entity


public class Cancion {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCancion;

    private String nombreCancion;
    private double precio;
    private double duracion;
    private double tamanio;
    private int calidadKbps;

    public Cancion() {}

    // Getters y Setters
    public int getIdCancion() { return idCancion; }
    public void setIdCancion(int idCancion) { this.idCancion = idCancion; }

    public String getNombreCancion() { return nombreCancion; }
    public void setNombreCancion(String nombreCancion) { this.nombreCancion = nombreCancion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) { this.duracion = duracion; }

    public double getTamanio() { return tamanio; }
    public void setTamanio(double tamanio) { this.tamanio = tamanio; }

    public int getCalidadKbps() { return calidadKbps; }
    public void setCalidadKbps(int calidadKbps) { this.calidadKbps = calidadKbps; }
}
