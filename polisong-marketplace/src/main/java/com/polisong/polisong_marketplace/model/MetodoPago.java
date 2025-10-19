package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "metodopago")
public class MetodoPago {

    // VARIABLES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodopago")
    private Integer idMetodoPago;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

  
    // CONSTRUCTOR

    public MetodoPago() {}

    // GETTERS Y SETTERS
    public Integer getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(Integer idMetodoPago) { this.idMetodoPago = idMetodoPago; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

